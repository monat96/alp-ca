package com.kt.nms.cctv.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.kt.nms.ping.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CCTVDto {
    private String cctvId;
    private String ipAddress;

    @JsonProperty("lng")
    private double longitude;

    @JsonProperty("lat")
    private double latitude;
    private String locationName;
    private String locationAddress;
    private String hlsAddress;
    private Status status;
}