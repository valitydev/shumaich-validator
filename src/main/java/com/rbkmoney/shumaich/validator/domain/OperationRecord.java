package com.rbkmoney.shumaich.validator.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OperationRecord extends Record {

    @Enumerated
    private OperationType operationType;
    private Long batchHash;

    public OperationRecord(RecordId recordId, OperationType operationType, Long batchHash) {
        this.id = recordId;
        this.operationType = operationType;
        this.batchHash = batchHash;
    }
}
