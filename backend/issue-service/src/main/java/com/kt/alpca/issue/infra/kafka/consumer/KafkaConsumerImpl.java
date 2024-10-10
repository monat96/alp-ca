//package com.kt.alpca.issue.infra.kafka.consumer;
//
//
//import com.kt.alpca.issue.domain.event.HealthCheckedEvent;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KafkaConsumerImpl implements KafkaConsumer {
//    @Override
//    @KafkaListener(topics = "health-check-event-in")
//    public void processHealthCheckedEvent(HealthCheckedEvent event) {
//        System.out.println("Processing health checked event: " + event.getCctvId());
//    }
//}
