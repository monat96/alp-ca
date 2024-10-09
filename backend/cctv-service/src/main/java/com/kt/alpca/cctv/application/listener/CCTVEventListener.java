package com.kt.alpca.cctv.application.listener;

import com.kt.alpca.cctv.common.KafkaBindingNames;
import com.kt.alpca.cctv.domain.event.CCTVRegisteredEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class CCTVEventListener {
    @Bean(name= KafkaBindingNames.CCTV_EVENT_FUNCTION)
    public Consumer<CCTVRegisteredEvent> cctvEventFunction() {
        return event -> System.out.println("CCTV Registered Event: " + event);
    }

}
