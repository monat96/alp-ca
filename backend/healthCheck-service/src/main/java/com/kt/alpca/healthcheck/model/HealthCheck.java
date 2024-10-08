package com.kt.alpca.healthcheck.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_check")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cctvId;

    private double rttMax;

    private double rttAvg;

    private double rttMin;

    private String icmpStatus; // TODO: enums 추가

    private String hlsStatus; // TODO: enums 추가

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
