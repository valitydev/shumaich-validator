package com.rbkmoney.shumaich.validator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FailureRecord {

    @EmbeddedId
    private RecordId id;

    @Enumerated(value = EnumType.STRING)
    private FailureReason reason;
}
