package com.rbkmoney.shumaich.validator.kafka.listener;

import com.rbkmoney.kafka.common.util.LogUtil;
import com.rbkmoney.shumaich.validator.domain.OperationLog;
import com.rbkmoney.shumaich.validator.kafka.handler.OperationLogHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OperationLogKafkaListener {

    private final OperationLogHandler operationLogHandler;

    @KafkaListener(topics = "${kafka.topic.id}", containerFactory = "kafkaListenerContainerFactory")
    public void handle(List<ConsumerRecord<String, OperationLog>> messages, Acknowledgment ack) {
        log.info("Got operationLog batch with size: {}", messages.size());
        operationLogHandler.handleEvents(messages);
        ack.acknowledge();
        log.info("Batch has been committed, size={}, {}", messages.size(), LogUtil.toSummaryString(messages));
    }

}
