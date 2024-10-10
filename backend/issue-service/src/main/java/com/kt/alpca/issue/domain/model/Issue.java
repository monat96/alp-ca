package com.kt.alpca.issue.domain.model;


import com.kt.alpca.issue.domain.model.enums.HealthCheckStatus;
import com.kt.alpca.issue.domain.model.enums.HealthCheckType;
import com.kt.alpca.issue.domain.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "issue")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cctvId;

    private Long healthCheckId; // HealthCheck ID

    @Enumerated(EnumType.STRING)
    private Status status; // 장애 상태

    private String details; // 조치 내용

    @Enumerated(EnumType.STRING)
    private HealthCheckType healthCheckType; // HealthCheck 종류

    @Enumerated(EnumType.STRING)
    private HealthCheckStatus healthCheckStatus; // HealthCheck 상태

    private LocalDateTime issuedAt; // 장애 발생 시간

    private LocalDateTime resolvedAt; // 장애 해결 시간

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
