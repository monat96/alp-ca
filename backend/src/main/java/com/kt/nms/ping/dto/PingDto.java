package com.kt.nms.ping.dto;

import com.kt.nms.ping.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PingDto {
    private float rttMax;
    private float rttMin;
    private float rttAvg;
    private float packetLossRate;
    private Status status;
    private String requestTime;
}
