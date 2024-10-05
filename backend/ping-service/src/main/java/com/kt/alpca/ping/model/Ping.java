// TODO: FK 추가 (CCTV ID)

package com.kt.alpca.ping.model;


import com.kt.alpca.ping.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "ping")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private double rttMax;

    @Column(nullable = false)
    private float rttAvg; // 평균 RTT

    @Column(nullable = false)
    private float rttMin; // 최소 RTT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // 상태

    @Column(nullable = false)
    private float packetLossRate; // 패킷손실율

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일시
}
