package com.kt.nms.ping.dto;

import com.kt.nms.ping.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class PingStatusResponse {
    private LocalDateTime requestTime;
    private Map<Status, Long> status;
}
