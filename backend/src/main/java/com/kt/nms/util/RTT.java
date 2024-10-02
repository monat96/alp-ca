package com.kt.nms.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RTT {
    private float min = 0.f;
    private float avg = 0.f;
    private float max = 0.f;
}
