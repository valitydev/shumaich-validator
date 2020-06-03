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

    public static final Long KAFKA_EARLY_OFFSET = 0L;
    public static final Long KAFKA_FAR_OFFSET = 1000L;

    public static OperationLog operationLog(String account, String plan, Long batch, Long batchHash, OperationType operationType) {
        return OperationLog.builder()
                .account(new Account(account, "RUB"))
                .planId(plan)
                .batchId(batch)
                .batchHash(batchHash)
                .operationType(operationType)
                .build();
    }

    public static LogWithOffset logWithOffset(String accountId, String planId, Long batchId, Long batchHash, OperationType operationType) {
        return LogWithOffset.builder()
                .account(new Account(accountId, "RUB"))
                .planId(planId)
                .batchId(batchId)
                .batchHash(batchHash)
                .operationType(operationType)
                .build();
    }

    public static LogWithOffset logWithOffset(Long batchHash) {
        return LogWithOffset.builder()
                .batchHash(batchHash)
                .build();
    }

    public static LogWithOffset logWithOffset(OperationType operationType) {
        return LogWithOffset.builder()
                .operationType(operationType)
                .build();
    }

    public static OperationRecord operationRecord(String accountId, String planId, Long batchId, Long batchHash, OperationType operationType, Long offset) {
        return new OperationRecord(new RecordId(planId, batchId, accountId), operationType, batchHash, offset);
    }

    public static OperationRecord operationRecord(String accountId, String planId, Long batchId) {
        return new OperationRecord(new RecordId(planId, batchId, accountId), null, null, null);
    }

    public static OperationRecord operationRecord(OperationType operationType) {
        return new OperationRecord(null, operationType, null, null);
    }

    public static OperationRecord operationRecord(Long batchHash) {
        return new OperationRecord(null, null, batchHash, null);
    }

}
