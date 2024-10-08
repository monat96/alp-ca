package com.kt.alpca.healthcheck.model;

import com.kt.alpca.healthcheck.enums.HLSStatus;
import com.kt.alpca.healthcheck.enums.ICMPStatus;
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
@Table(name = "health_check")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class HealthCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cctvId;

    private double rttMax;

    private double rttAvg;

    private double rttMin;

    @Enumerated(EnumType.STRING)
    private ICMPStatus icmpStatus;

    @Enumerated(EnumType.STRING)
    private HLSStatus hlsStatus;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
