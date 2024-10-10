package com.kt.alpca.issue.domain.event;

import com.kt.alpca.issue.domain.model.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueCreatedEvent {
    private String cctvId;
    private Long issueId;
    private Long healthCheckId;
    private String receiverId;
    private String description;
}
