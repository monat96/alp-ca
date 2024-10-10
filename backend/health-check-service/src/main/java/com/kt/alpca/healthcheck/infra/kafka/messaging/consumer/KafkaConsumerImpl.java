package com.kt.alpca.healthcheck.infra.kafka.messaging.consumer;

import com.kt.alpca.healthcheck.application.service.HealthCheckService;
import com.kt.alpca.healthcheck.domain.event.CCTVRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Configuration
@RequiredArgsConstructor
public class KafkaConsumerImpl {
    private final HealthCheckService healthCheckService;

//    @Bean
//    public Consumer<CCTVRegisteredEvent> healthCheckEventFunction() {
//        return event -> {
//            System.out.println(event);
//        };
//    }
}
