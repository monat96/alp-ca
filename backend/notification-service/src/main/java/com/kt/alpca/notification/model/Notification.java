package com.kt.alpca.notification.model;

import com.kt.alpca.notification.enums.Status;
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
@Table(name = "notification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long issueId;

    private Long healthCheckId;

    private String receiverId; // 수신자 정보

    private String description; // 알림 내용

    @Enumerated(EnumType.STRING)
    private Status status; // 알림 상태

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 알림 생성일시

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 알림 수정일시

    private LocalDateTime sentAt;// 알림 발송일시
}
