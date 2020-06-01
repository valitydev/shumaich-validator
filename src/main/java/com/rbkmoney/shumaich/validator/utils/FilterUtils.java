package com.rbkmoney.shumaich.validator.utils;

import com.rbkmoney.shumaich.validator.domain.OperationLog;
import com.rbkmoney.shumaich.validator.domain.OperationRecord;
import com.rbkmoney.shumaich.validator.domain.RecordId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterUtils {

    public static List<OperationRecord> filterOperationRecordByRecordId(List<OperationRecord> operationRecords, RecordId recordId) {
        return operationRecords.stream()
                .filter(operationRecord -> operationRecord.getId().equals(recordId))
                .collect(Collectors.toList());
    }

}
