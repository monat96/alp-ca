package com.kt.alpca.issue.application.service;

import com.kt.alpca.issue.domain.event.HealthCheckedEvent;
import com.kt.alpca.issue.domain.event.IssueCreatedEvent;
import com.kt.alpca.issue.domain.model.Issue;
import com.kt.alpca.issue.domain.model.enums.HealthCheckStatus;
import com.kt.alpca.issue.domain.model.enums.HealthCheckType;
import com.kt.alpca.issue.domain.model.enums.NotificationStatus;
import com.kt.alpca.issue.domain.model.enums.Status;
import com.kt.alpca.issue.infra.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final StreamBridge streamBridge;

    public void performHealthCheckedEvent(HealthCheckedEvent event) {
        Issue issue = Issue.builder()
                .cctvId(event.getCctvId())
                .healthCheckId(event.getHealthCheckId())
                .status(Status.OPEN)
                .healthCheckStatus(HealthCheckStatus.valueOf(event.getStatus()))
                .healthCheckType(event.getHealthCheckType())
                .issuedAt(event.getIssuedAt())
                .build();

        issueRepository.save(issue);

        IssueCreatedEvent issueCreatedEvent = IssueCreatedEvent
                .builder()
                .cctvId(event.getCctvId())
                .issueId(issue.getId())
                .healthCheckId(event.getHealthCheckId())
                .receiverId(issue.getHealthCheckType() == HealthCheckType.HLS ? "CCTV" : "NETWORK")
                .description("Health Check Failed")
                .build();


        streamBridge.send("issue-created-topic", issueCreatedEvent);

    }
}
