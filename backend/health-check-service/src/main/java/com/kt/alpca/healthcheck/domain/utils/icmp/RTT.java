package com.kt.alpca.healthcheck.domain.utils.icmp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RTT {
    private float min = 0.f;
    private float avg = 0.f;
    private float max = 0.f;
}
