package com.kt.alpca.issue.domain.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.kt.alpca.issue.domain.model.enums.HealthCheckType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckedEvent {
    private String cctvId;
    private Long healthCheckId;
    private String status;
    private HealthCheckType healthCheckType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime issuedAt;
}
