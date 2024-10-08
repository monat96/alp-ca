package com.kt.alpca.healthcheck.utils.icmp;

import com.kt.alpca.healthcheck.enums.ICMPStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EchoReply {
    private RTT rtt;
    private float packetLossRate;
    private boolean isReachable;

    public ICMPStatus getICMPStatus() {
        if (!isReachable) return ICMPStatus.TIMEOUT;
        if (packetLossRate == 100) return ICMPStatus.FAIL;
        if (packetLossRate > 0) return ICMPStatus.LOSS;
        return ICMPStatus.SUCCESS;
    }
}
