package com.rbkmoney.shumaich.validator.utils;

import com.rbkmoney.shumaich.validator.TestData;
import com.rbkmoney.shumaich.validator.domain.OperationLog;
import com.rbkmoney.shumaich.validator.domain.OperationRecord;
import com.rbkmoney.shumaich.validator.domain.RecordId;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class FilterUtilsTest {

    @Test
    public void operationRecordsFiltered() {
        final List<OperationRecord> operationRecords = List.of(TestData.operationRecord("acc1", "plan1", 1L),
                TestData.operationRecord("acc1", "plan1", 2L),
                TestData.operationRecord("acc2", "plan1", 1L),
                TestData.operationRecord("acc1", "plan2", 1L));

        Assert.assertEquals(
                1,
                FilterUtils.filterOperationRecordByRecordId(operationRecords, new RecordId("plan1", 1L, "acc2")).size()
        );
    }

    @Test
    public void operationLogsFiltered() {
        final List<OperationLog> operationLogs = List.of(TestData.operationLog("acc1", "plan1", 1L, null, null),
                TestData.operationLog("acc1", "plan1", 2L, null, null),
                TestData.operationLog("acc2", "plan1", 1L, null, null),
                TestData.operationLog("acc1", "plan2", 1L, null, null));

        Assert.assertEquals(
                1,
                FilterUtils.filterOperationLogByRecordId(operationLogs, new RecordId("plan1", 1L, "acc2")).size()
        );
    }

}