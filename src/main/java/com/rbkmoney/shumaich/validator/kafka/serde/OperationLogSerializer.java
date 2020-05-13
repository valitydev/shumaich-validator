package com.rbkmoney.shumaich.validator.kafka.serde;

import com.rbkmoney.shumaich.domain.OperationLog;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class OperationLogSerializer extends JsonSerializer<OperationLog> {}
