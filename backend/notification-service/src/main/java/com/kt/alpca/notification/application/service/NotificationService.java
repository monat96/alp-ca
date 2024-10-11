package com.kt.alpca.notification.application.service;

import com.kt.alpca.notification.domain.event.IssueCreatedEvent;
import com.kt.alpca.notification.domain.model.Notification;
import com.kt.alpca.notification.domain.model.enums.Status;
import com.kt.alpca.notification.infra.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void performIssueCreatedEvent(IssueCreatedEvent issueCreatedEvent) {

        Notification notification = Notification.builder()
                .cctvId(issueCreatedEvent.getCctvId())
                .issueId(issueCreatedEvent.getIssueId())
                .healthCheckId(issueCreatedEvent.getHealthCheckId())
                .receiverId(issueCreatedEvent.getReceiverId())
                .status(Status.CREATED)
                .description(issueCreatedEvent.getDescription())
                .build();

        notificationRepository.save(notification);


    }
}
