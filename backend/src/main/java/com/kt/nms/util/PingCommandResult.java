package com.kt.nms.util;

import com.kt.nms.ping.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@Builder
@AllArgsConstructor
public class PingCommandResult {
    private RTT rtt;
    private float packetLossRate;
    private boolean isReachable;

    public Status getStatus() {
////        if (!isReachable) return Status.TIMEOUT;
////        if (packetLossRate == 100) return Status.FAIL;
////        if (packetLossRate > 0) return Status.LOSS;
////        return Status.SUCCESS;
        // 비율을 고려하여 Status 리스트를 생성 (7:1:1:1 비율)
        List<Status> weightedStatuses = new ArrayList<>();

        // Status.SUCCESS 7번 추가
        for (int i = 0; i < 7; i++) {
            weightedStatuses.add(Status.SUCCESS);
        }

        // 나머지 Status 값들 1번씩 추가
        weightedStatuses.add(Status.TIMEOUT);
        weightedStatuses.add(Status.FAIL);
        weightedStatuses.add(Status.LOSS);

        // Random 객체 생성
        Random random = new Random();

        // 리스트에서 무작위로 하나 선택하여 반환
        return weightedStatuses.get(random.nextInt(weightedStatuses.size()));
    }
}