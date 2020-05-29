package com.rbkmoney.shumaich.validator.repo;

import com.rbkmoney.shumaich.validator.domain.FailureRecord;
import com.rbkmoney.shumaich.validator.domain.RecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailureRecordRepo extends JpaRepository<FailureRecord, RecordId> {
}
