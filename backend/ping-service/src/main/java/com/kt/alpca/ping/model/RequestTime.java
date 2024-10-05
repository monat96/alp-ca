package com.kt.alpca.ping.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_time")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false, updatable = false)
    private LocalDateTime time;
}
