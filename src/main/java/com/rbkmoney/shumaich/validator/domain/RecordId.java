package com.rbkmoney.shumaich.validator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordId implements Serializable {

    private String planId;
    private Long batchId;
    private String accountId;

    public RecordId(OperationLog operationLog) {
        this.planId = operationLog.getPlanId();
        this.batchId = operationLog.getBatchId();
        this.accountId = operationLog.getAccount().getId();
    }
}
