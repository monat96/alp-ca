package com.kt.alpca.healthcheck.application.listener;

import com.kt.alpca.healthcheck.application.service.HealthCheckService;
import com.kt.alpca.healthcheck.domain.event.CCTVRegisteredEvent;
import com.kt.alpca.healthcheck.infra.kafka.messaging.consumer.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class HealthCheckEventListener {
    private final HealthCheckService healthCheckService;

    @Bean
    public Consumer<CCTVRegisteredEvent> healthCheckEvent() {
        return healthCheckService::performHealthCheck;
    }
}
