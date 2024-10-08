package com.kt.alpca.issue.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "issue")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cctvId;

    private String healthCheckId; // HealthCheck ID

    // TODO: enums 추가
    private String status; // 장애 상태

    private String details; // 조치 내용

    // TODO: enums 추가
    private String healthCheckType; // HealthCheck 종류

    private LocalDateTime issueAt; // 장애 발생 시간

    private LocalDateTime resolveAt; // 장애 해결 시간

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updateAt;
}
