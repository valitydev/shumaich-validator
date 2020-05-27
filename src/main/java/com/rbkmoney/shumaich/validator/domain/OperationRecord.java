package com.rbkmoney.shumaich.validator.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
