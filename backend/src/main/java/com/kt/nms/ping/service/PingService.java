package com.kt.nms.ping.service;

import com.kt.nms.cctv.model.CCTV;
import com.kt.nms.cctv.repository.CCTVRepository;
import com.kt.nms.ping.dto.PingDto;
import com.kt.nms.ping.dto.PingStatusResponse;
import com.kt.nms.ping.enums.Status;
import com.kt.nms.ping.model.Ping;
import com.kt.nms.ping.repository.PingRepository;
import com.kt.nms.requestTime.model.RequestTime;
import com.kt.nms.requestTime.repository.RequestTimeRepository;
import com.kt.nms.util.PingCommandResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PingService {
    private final PingRepository pingRepository;
    private final CCTVRepository cctvRepository;
    private final RequestTimeRepository requestTimeRepository;

    public Ping create(CCTV cctv, RequestTime requestTime, PingCommandResult pingCommandResult) {
        var ping = Ping.builder()
                .cctv(cctv)
                .rttMax(pingCommandResult.getRtt().getMax())
                .rttMin(pingCommandResult.getRtt().getMin())
                .rttAvg(pingCommandResult.getRtt().getAvg())
                .packetLossRate(pingCommandResult.getPacketLossRate())
                .status(pingCommandResult.getStatus())
                .requestTime(requestTime)
                .build();

        return pingRepository.save(ping);
    }

    public List<PingDto> getPingByCctvId(String cctvId) {
        var cctv = cctvRepository.findByCctvId(cctvId).orElseThrow();
        List<Ping> pingList = pingRepository.findByCctv(cctv);
        return pingList.stream()
                .sorted(Comparator.comparing(ping -> ping.getRequestTime().getRequestTime())) // requestTime 기준 정렬
                .map(this::convertToPingDto).collect(Collectors.toList());
    }

    @Transactional
    public void saveAll(List<? extends Ping> pings) {
        pingRepository.saveAll(pings);
    }

    private PingDto convertToPingDto(Ping ping) {
        return PingDto.builder()
                .rttMax(ping.getRttMax())
                .rttMin(ping.getRttMin())
                .rttAvg(ping.getRttAvg())
                .packetLossRate(ping.getPacketLossRate())
                .status(ping.getStatus())
                .requestTime(ping.getRequestTime().getRequestTime().toString())
                .build();
    }

    public PingStatusResponse getPingStatus() {
        RequestTime lastestRequestTime = requestTimeRepository.findTopByOrderByRequestTimeDesc();
        List<Ping> pingList = pingRepository.findByRequestTime(lastestRequestTime);

        Map<Status, Long> pingStatusCount = new EnumMap<>(Status.class);
        Stream.of(Status.values()).forEach(status -> pingStatusCount.put(status, 0L));

        Map<Status, Long> countedStatuses = pingList.stream()
                .collect(Collectors.groupingBy(Ping::getStatus, Collectors.counting()));

        pingStatusCount.putAll(countedStatuses);

        return PingStatusResponse.builder()
                .requestTime(lastestRequestTime.getRequestTime())
                .status(pingStatusCount)
                .build();
    }
}