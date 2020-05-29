package com.rbkmoney.shumaich.validator.utils;

import com.rbkmoney.shumaich.validator.domain.OperationLog;
import com.rbkmoney.shumaich.validator.domain.OperationRecord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

import static com.rbkmoney.shumaich.validator.domain.OperationType.HOLD;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckUtils {
    
    public static boolean finalOperationsMixed(List<OperationRecord> operationRecords, List<OperationLog> finalOps) {
        return Stream.concat(
                finalOps.stream().map(OperationLog::getOperationType),
                operationRecords.stream().map(OperationRecord::getOperationType)
        )
                .filter(operationType -> !operationType.equals(HOLD))
                .distinct()
                .count() == 2;
    }

    public static boolean containsFinalOp(List<OperationRecord> dbRecords) {
        return dbRecords.stream().anyMatch(operationRecord -> !operationRecord.getOperationType().equals(HOLD));
    }

    public static boolean containsHold(List<OperationRecord> dbRecords) {
        return dbRecords.stream().anyMatch(operationRecord -> operationRecord.getOperationType().equals(HOLD));
    }

    public static boolean checksumConsistent(List<OperationLog> logs) {
        return logs.stream().map(OperationLog::getBatchHash).distinct().count() == 1;
    }

    public static boolean checksumConsistent(List<OperationRecord> records, List<OperationLog> logs) {
        return Stream.concat(
                logs.stream().map(OperationLog::getBatchHash),
                records.stream().map(OperationRecord::getBatchHash)
        ).distinct().count() == 1;
    }
    
    
}
