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

    public ICMPStatus getICMPStatus() {
//        if (!isReachable) return ICMPStatus.TIMEOUT;
//        if (packetLossRate == 100) return ICMPStatus.FAIL;
//        if (packetLossRate > 0) return ICMPStatus.LOSS;
//        return ICMPStatus.SUCCESS;
        List<ICMPStatus> weightedStatuses = new ArrayList<>();

        // Status.SUCCESS 7번 추가
        for (int i = 0; i < 7; i++) {
            weightedStatuses.add(ICMPStatus.SUCCESS);
        }

        // 나머지 Status 값들 1번씩 추가
        weightedStatuses.add(ICMPStatus.TIMEOUT);
        weightedStatuses.add(ICMPStatus.FAIL);
        weightedStatuses.add(ICMPStatus.LOSS);


        // 리스트에서 무작위로 하나 선택하여 반환
        return weightedStatuses.get(TEST_RANDOM.nextInt(weightedStatuses.size()));
    }
}
