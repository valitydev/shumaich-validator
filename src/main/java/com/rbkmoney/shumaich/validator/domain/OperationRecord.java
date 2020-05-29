package com.rbkmoney.shumaich.validator.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

@Entity
@Data
@NoArgsConstructor
public class OperationRecord {

    @EmbeddedId
    RecordId id;

    @Enumerated
    private OperationType operationType;
    private Long batchHash;

    public OperationRecord(RecordId recordId, OperationType operationType, Long batchHash) {
        this.id = recordId;
        this.operationType = operationType;
        this.batchHash = batchHash;
    }
}
