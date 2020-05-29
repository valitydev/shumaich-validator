package com.rbkmoney.shumaich.validator.kafka.handler;

import com.rbkmoney.shumaich.validator.dao.RecordDao;
import com.rbkmoney.shumaich.validator.domain.*;
import com.rbkmoney.shumaich.validator.repo.OperationRecordRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rbkmoney.shumaich.validator.domain.OperationType.*;
import static com.rbkmoney.shumaich.validator.utils.CheckUtils.*;
import static com.rbkmoney.shumaich.validator.utils.FilterUtils.filterOperationLogByRecordId;
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

        final Map<OperationType, List<OperationLog>> arrivedOperationLogsGroupedByOperationType = messages.stream()
                .map(ConsumerRecord::value)
                .collect(Collectors.groupingBy(OperationLog::getOperationType));

        for (RecordId recordId : operationLogsGroupedByRecordId.keySet()) {

            final List<OperationLog> holds = filterOperationLogByRecordId(arrivedOperationLogsGroupedByOperationType.getOrDefault(HOLD, List.of()), recordId);
            final List<OperationLog> commits = filterOperationLogByRecordId(arrivedOperationLogsGroupedByOperationType.getOrDefault(COMMIT, List.of()), recordId);
            final List<OperationLog> rollbacks = filterOperationLogByRecordId(arrivedOperationLogsGroupedByOperationType.getOrDefault(ROLLBACK, List.of()), recordId);
            List<OperationRecord> dbRecords = filterOperationRecordByRecordId(operationRecordsInDb, recordId);

            List<FailureRecord> failureRecords = new ArrayList<>();

            processHolds(recordId, holds, dbRecords, failureRecords);
            processFinalOps(recordId, commits, dbRecords, failureRecords);
            processFinalOps(recordId, rollbacks, dbRecords, failureRecords);

            if ((!commits.isEmpty() && !rollbacks.isEmpty())) {
                failureRecords.add(getFailureRecord(recordId, FailureReason.FINAL_OPERATIONS_MIXED));
            }

            recordDao.save(dbRecords, failureRecords);
        }

    }

    private void processHolds(RecordId recordId, List<OperationLog> holdsForRecordId, List<OperationRecord> dbRecords, List<FailureRecord> failureRecords) {
        if (holdsForRecordId.isEmpty()) {
            return;
        }

        if (dbRecords.isEmpty()) {
            if (checksumConsistent(holdsForRecordId)) {
                dbRecords.add(getOperationRecord(holdsForRecordId));
            } else {
                failureRecords.add(getFailureRecord(recordId, FailureReason.DIFFERENT_OPERATION_ALREADY_EXISTS));
                dbRecords.add(getOperationRecord(List.of(holdsForRecordId.get(0)))); // we had an empty base, but there 2 different holds. So write 1 of them.
            }
        }

        if (containsHold(dbRecords) && !checksumConsistent(dbRecords, holdsForRecordId)) {
            failureRecords.add(getFailureRecord(recordId, FailureReason.DIFFERENT_OPERATION_ALREADY_EXISTS));
        }

        if (containsFinalOp(dbRecords)) {
            failureRecords.add(getFailureRecord(recordId, FailureReason.HOLD_AFTER_FINAL_OPERATION));
        }
    }

    private void processFinalOps(RecordId recordId, List<OperationLog> finalOps, List<OperationRecord> dbRecords, List<FailureRecord> failureRecords) {
        if (finalOps.isEmpty()) {
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
