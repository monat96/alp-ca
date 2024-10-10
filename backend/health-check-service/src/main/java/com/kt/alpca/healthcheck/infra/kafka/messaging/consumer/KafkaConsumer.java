package com.kt.alpca.healthcheck.infra.kafka.messaging.consumer;

import com.kt.alpca.healthcheck.domain.event.CCTVRegisteredEvent;
import org.springframework.messaging.handler.annotation.Payload;

public interface KafkaConsumer {
    void processCCTVRegisteredEvent(
            @Payload CCTVRegisteredEvent cctvRegisteredEvent
    );

}
