package com.rbkmoney.shumaich.validator.dao;

import com.rbkmoney.shumaich.validator.domain.FailureRecord;
import com.rbkmoney.shumaich.validator.domain.OperationRecord;
import com.rbkmoney.shumaich.validator.repo.FailureRecordRepo;
import com.rbkmoney.shumaich.validator.repo.OperationRecordRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@AllArgsConstructor
public class RecordDao {

    final OperationRecordRepo operationRecordRepo;
    final FailureRecordRepo failureRecordRepo;

    @Transactional
    public void save(List<OperationRecord> operationRecords, List<FailureRecord> failureRecords) {
        operationRecordRepo.saveAll(operationRecords);
        failureRecordRepo.saveAll(failureRecords);
    }
}
