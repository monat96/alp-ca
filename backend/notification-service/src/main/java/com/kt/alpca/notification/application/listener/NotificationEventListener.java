package com.kt.alpca.notification.application.listener;

import com.kt.alpca.notification.application.service.NotificationService;
import com.kt.alpca.notification.domain.event.IssueCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    @Bean
    public Consumer<IssueCreatedEvent> notificationEvent() {
        return notificationService::performIssueCreatedEvent;
    }

}
