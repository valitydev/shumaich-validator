package com.rbkmoney.shumaich.validator.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Entity
@NoArgsConstructor
public class FailureRecord {

    @EmbeddedId
    RecordId id;

    @Enumerated(value = EnumType.STRING)
    private FailureReason reason;

    public FailureRecord(RecordId recordId, FailureReason failureReason) {
        this.id = recordId;
        this.reason = failureReason;
    }
}
