package com.rbkmoney.shumaich.validator;

import com.rbkmoney.shumaich.validator.repo.OperationRecordRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShumaichValidatorApplication.class)
public class ShumaichValidatorApplicationTest {

    @Autowired
    OperationRecordRepo operationRecordRepo;

    @Test
    public void contextLoads() {
        operationRecordRepo.findAll();
    }
}
