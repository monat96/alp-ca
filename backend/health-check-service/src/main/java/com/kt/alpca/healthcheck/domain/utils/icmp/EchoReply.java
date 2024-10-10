package com.kt.alpca.healthcheck.domain.utils.icmp;

import com.kt.alpca.healthcheck.domain.model.enums.ICMPStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EchoReply {
    private static final Random TEST_RANDOM = new Random();

    private RTT rtt;
    private float packetLossRate;
    private boolean isReachable;
    private ICMPStatus status;
}
