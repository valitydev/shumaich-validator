package com.rbkmoney.shumaich.validator;

import com.rbkmoney.shumaich.validator.domain.*;

public class TestData {

    public static final String OPERATION_LOG_TOPIC = "operation_log";

    public static final String ACCOUNT_1 = "acc1";
    public static final String ACCOUNT_2 = "acc2";

    public static final String PLAN_1 = "plan1";
    public static final String PLAN_2 = "plan2";

    public static final Long BATCH_1 = 1L;

    public static final Long HASH_1 = 111L;
    public static final Long HASH_2 = 222L;

    public static OperationLog operationLog(String accountId, String planId, Long batchId, Long batchHash, OperationType operationType) {
        return OperationLog.builder()
                .account(new Account(accountId, "RUB"))
                .planId(planId)
                .batchId(batchId)
                .batchHash(batchHash)
                .operationType(operationType)
                .build();
    }

    public static OperationLog operationLog(Long batchHash) {
        return OperationLog.builder()
                .batchHash(batchHash)
                .build();
    }

    public static OperationLog operationLog(OperationType operationType) {
        return OperationLog.builder()
                .operationType(operationType)
                .build();
    }

    public static OperationRecord operationRecord(String accountId, String planId, Long batchId, Long batchHash, OperationType operationType) {
        return new OperationRecord(new RecordId(planId, batchId, accountId), operationType, batchHash);
    }

    public static OperationRecord operationRecord(String accountId, String planId, Long batchId) {
        return new OperationRecord(new RecordId(planId, batchId, accountId), null, null);
    }

    public static OperationRecord operationRecord(OperationType operationType) {
        return new OperationRecord(null, operationType, null);
    }

    public static OperationRecord operationRecord(Long batchHash) {
        return new OperationRecord(null, null, batchHash);
    }
}
