package com.rbkmoney.shumaich.validator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationRecord {

    @EmbeddedId
    private RecordId id;

    @Enumerated
    private OperationType operationType;
    private Long batchHash;
    private Long kafkaOffset;
}
