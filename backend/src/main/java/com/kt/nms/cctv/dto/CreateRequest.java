package com.kt.nms.cctv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequest {
    private String cctvId;
    private String ipAddress;
    private double longitude;
    private double latitude;
    private String locationName;
    private String locationAddress;
    private String hlsAddress;
}
