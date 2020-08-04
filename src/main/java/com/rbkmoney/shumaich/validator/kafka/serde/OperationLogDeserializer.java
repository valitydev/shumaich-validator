package com.rbkmoney.shumaich.validator.kafka.serde;


import com.rbkmoney.damsel.shumaich.OperationLog;
import com.rbkmoney.kafka.common.serialization.AbstractThriftDeserializer;

public class OperationLogDeserializer extends AbstractThriftDeserializer<OperationLog> {

    @Override
    public OperationLog deserialize(String topic, byte[] data) {
        return deserialize(data, new OperationLog());
    }
}
