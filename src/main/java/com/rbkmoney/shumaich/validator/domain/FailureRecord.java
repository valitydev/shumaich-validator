package com.rbkmoney.shumaich.validator.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class FailureRecord extends Record {

    @Enumerated(value = EnumType.STRING)
    private FailureReason reason;

    public FailureRecord(RecordId recordId, FailureReason failureReason) {
        this.id = recordId;
        this.reason = failureReason;
    }
}
