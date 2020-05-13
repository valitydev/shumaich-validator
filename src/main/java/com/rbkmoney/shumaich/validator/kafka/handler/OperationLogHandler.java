package com.rbkmoney.shumaich.validator.kafka.handler;

import com.rbkmoney.shumaich.validator.domain.OperationLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationLogHandler {

    List<Check> checkers;

    public void handleEvents(List<ConsumerRecord<String, OperationLog>> messages) {
        //load messages from db in batch

        //check all messages

        //write to common table that succeeded, write to failed other
    }

}
