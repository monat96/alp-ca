package com.kt.alpca.healthcheck.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CCTVRegisteredEvent {
    private String cctvId;
    private String ipAddress;
    private String hlsAddress;
}
