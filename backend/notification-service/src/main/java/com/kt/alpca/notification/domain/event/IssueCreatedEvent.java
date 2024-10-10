package com.kt.alpca.notification.domain.event;

import com.kt.alpca.notification.domain.model.enums.Status;
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
    private Status status;
}
