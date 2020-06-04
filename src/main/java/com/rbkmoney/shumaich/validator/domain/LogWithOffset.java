package com.rbkmoney.shumaich.validator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Data
@Builder
@AllArgsConstructor
public class LogWithOffset {

    private String planId;
    private Long batchId;
    private OperationType operationType;
    private Account account;
    private Long batchHash;
    private ValidationStatus validationStatus;

    private Long kafkaOffset;

    public LogWithOffset(ConsumerRecord<String, OperationLog> consumerRecord) {
        final OperationLog operationLog = consumerRecord.value();
        this.planId = operationLog.getPlanId();
        this.batchId = operationLog.getBatchId();
        this.operationType = operationLog.getOperationType();
        this.account = operationLog.getAccount();
        this.batchHash = operationLog.getBatchHash();
        this.validationStatus = operationLog.getValidationStatus();
        this.kafkaOffset = consumerRecord.offset();
    }
}
