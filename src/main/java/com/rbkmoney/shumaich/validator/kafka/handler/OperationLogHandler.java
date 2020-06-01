package com.rbkmoney.shumaich.validator.kafka.handler;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Streams;
import com.rbkmoney.shumaich.validator.dao.RecordDao;
import com.rbkmoney.shumaich.validator.domain.*;
import com.rbkmoney.shumaich.validator.repo.OperationRecordRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rbkmoney.shumaich.validator.domain.OperationType.*;
import static com.rbkmoney.shumaich.validator.utils.CheckUtils.*;
import static com.rbkmoney.shumaich.validator.utils.FilterUtils.filterOperationRecordByRecordId;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationLogHandler {

    private final RecordDao recordDao;
    private final OperationRecordRepo operationRecordRepo;

    public void handleEvents(List<ConsumerRecord<String, OperationLog>> messages) {
        //load messages from db in batch
        final Map<RecordId, List<OperationLog>> operationLogsGroupedByRecordId = messages.stream()
                .map(ConsumerRecord::value)
                .collect(Collectors.groupingBy(RecordId::new));

        List<OperationRecord> operationRecordsInDb = operationRecordRepo.findAllById(operationLogsGroupedByRecordId.keySet());

        ImmutableTable<RecordId, OperationType, List<OperationLog>> recordIdOperationTypeTable = messages.stream()
                .map(ConsumerRecord::value)
                .collect(ImmutableTable.toImmutableTable(
                        RecordId::new,
                        OperationLog::getOperationType,
                        Arrays::asList,
                        (a, b) -> Streams.concat(a.stream(), b.stream()).collect(Collectors.toList())));

        List<FailureRecord> failureRecordsToSave = new ArrayList<>();
        List<OperationRecord> dbRecordsToSave = new ArrayList<>();

        for (RecordId recordId : operationLogsGroupedByRecordId.keySet()) {

            final List<OperationLog> holds = recordIdOperationTypeTable.get(recordId, HOLD);
            final List<OperationLog> commits = recordIdOperationTypeTable.get(recordId, COMMIT);
            final List<OperationLog> rollbacks = recordIdOperationTypeTable.get(recordId, ROLLBACK);

            List<OperationRecord> dbRecords = filterOperationRecordByRecordId(operationRecordsInDb, recordId);


            processHolds(recordId, holds, dbRecords, failureRecordsToSave);
            processFinalOps(recordId, commits, dbRecords, failureRecordsToSave);
            processFinalOps(recordId, rollbacks, dbRecords, failureRecordsToSave);

            if (commits != null && rollbacks != null &&
                    (!commits.isEmpty() && !rollbacks.isEmpty())) {
                failureRecordsToSave.add(getFailureRecord(recordId, FailureReason.FINAL_OPERATIONS_MIXED));
            }
            dbRecordsToSave.addAll(dbRecords);
        }

        recordDao.save(dbRecordsToSave, failureRecordsToSave);
    }

    private void processHolds(RecordId recordId, List<OperationLog> holds, List<OperationRecord> dbRecords, List<FailureRecord> failureRecords) {
        if (holds == null || holds.isEmpty()) {
            return;
        }

        if (dbRecords.isEmpty()) {
            if (checksumConsistent(holds)) {
                dbRecords.add(getOperationRecord(holds));
            } else {
                failureRecords.add(getFailureRecord(recordId, FailureReason.DIFFERENT_OPERATION_ALREADY_EXISTS));
                dbRecords.add(getOperationRecord(List.of(holds.get(0)))); // we had an empty base, but there 2 different holds. So write 1 of them.
            }
        }

        if (containsHold(dbRecords) && !checksumConsistent(dbRecords, holds)) {
            failureRecords.add(getFailureRecord(recordId, FailureReason.DIFFERENT_OPERATION_ALREADY_EXISTS));
        }

        if (containsFinalOp(dbRecords)) {
            failureRecords.add(getFailureRecord(recordId, FailureReason.HOLD_AFTER_FINAL_OPERATION));
        }
    }

    private void processFinalOps(RecordId recordId, List<OperationLog> finalOps, List<OperationRecord> dbRecords, List<FailureRecord> failureRecords) {
        if (finalOps == null || finalOps.isEmpty()) {
            return;
        }

        if (dbRecords.isEmpty()) {
            failureRecords.add(getFailureRecord(recordId, FailureReason.HOLD_DOES_NOT_EXIST));
        }

        if (containsHold(dbRecords)) {
            if (checksumConsistent(dbRecords, finalOps)) {
                updateDbRecord(dbRecords, finalOps);
            } else {
                failureRecords.add(getFailureRecord(recordId, FailureReason.DIFFERENT_OPERATION_ALREADY_EXISTS));
            }
        }

        if (containsFinalOp(dbRecords)) {
            if (!checksumConsistent(dbRecords, finalOps)) {
                failureRecords.add(getFailureRecord(recordId, FailureReason.DIFFERENT_OPERATION_ALREADY_EXISTS));
            }
            if (finalOperationsMixed(dbRecords, finalOps)) {
                failureRecords.add(getFailureRecord(recordId, FailureReason.FINAL_OPERATIONS_MIXED));
            }
        }
    }

    private void updateDbRecord(List<OperationRecord> dbRecords, List<OperationLog> finalOps) {
        dbRecords.get(0).setOperationType(finalOps.get(0).getOperationType());
    }

    private OperationRecord getOperationRecord(List<OperationLog> holdsForRecordId) {
        final RecordId recordId = holdsForRecordId.stream().map(RecordId::new).findFirst().orElseThrow();
        return new OperationRecord(recordId, HOLD, holdsForRecordId.get(0).getBatchHash());
    }

    private FailureRecord getFailureRecord(RecordId recordId, FailureReason failureReason) {
        return new FailureRecord(recordId, failureReason);
    }

}
