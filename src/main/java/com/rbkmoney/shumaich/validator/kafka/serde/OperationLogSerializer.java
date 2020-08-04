package com.rbkmoney.shumaich.validator.kafka.serde;

import com.rbkmoney.damsel.shumaich.OperationLog;
import com.rbkmoney.kafka.common.serialization.ThriftSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class OperationLogSerializer extends ThriftSerializer<OperationLog> {
}
