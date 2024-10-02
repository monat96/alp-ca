package com.kt.nms.ping.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kt.nms.cctv.model.CCTV;
import com.kt.nms.ping.enums.Status;

import java.time.LocalDateTime;

import com.kt.nms.requestTime.model.RequestTime;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "ping")
@Getter
@Setter
@ToString(exclude = {"cctv", "requestTime"})
@EqualsAndHashCode(exclude = {"cctv", "requestTime"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Ping {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pingId; // PingTest 번호

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cctv_id", nullable = false)
  private CCTV cctv; // 연관된 CCTV

  @Column(nullable = false)
  private float rttMax; // 최대 RTT

  @Column(nullable = false)
  private float rttAvg; // 평균 RTT

  @Column(nullable = false)
  private float rttMin; // 최소 RTT

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status; // 상태

  @Column(nullable = false)
  private float packetLossRate; // 패킷손실율

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "request_time_id", nullable = false, updatable = false)
  private RequestTime requestTime; // 요청시간

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt; // 생성일자
}