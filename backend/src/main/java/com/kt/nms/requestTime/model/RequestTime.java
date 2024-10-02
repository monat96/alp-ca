package com.kt.nms.requestTime.model;

import com.kt.nms.ping.model.Ping;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Entity
@Table(name = "request_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestTimeId; // 요청시간 번호

    @Column(nullable = false, updatable = false)
    private LocalDateTime requestTime; // 요청시간
}
