package com.kt.nms.ping.service;

import com.kt.nms.cctv.model.CCTV;
import com.kt.nms.cctv.repository.CCTVRepository;
import com.kt.nms.requestTime.model.RequestTime;
import com.kt.nms.requestTime.repository.RequestTimeRepository;
import com.kt.nms.util.PingCommand;
import com.kt.nms.util.PingCommandResult;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PingSchedulerService {
    private final PingService pingService;
    private final CCTVRepository cctvRepository;
    private final RequestTimeRepository requestTimeRepository;


    private final ExecutorService executorService = Executors.newFixedThreadPool(30);

    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void schedulePingTask() {
        RequestTime requestTime = RequestTime.builder().requestTime(LocalDateTime.now()).build();
        List<CCTV> cctvList = cctvRepository.findAll();
        cctvList.forEach(cctv -> executorService.submit(() -> executePingTask(cctv, requestTime)));
        requestTimeRepository.save(requestTime);
    }

    @PreDestroy
    public void shutdownExecutor() {
        log.info("Shutting down ExecutorService...");
        executorService.shutdown();
    }

    private void executePingTask(CCTV cctv, RequestTime requestTime) {
        try {
            log.info("Ping task started for CCTV: {}", cctv.getCctvId());
            PingCommandResult pingCommandResult = PingCommand.exec(cctv.getIpAddress());
            pingService.create(cctv, requestTime, pingCommandResult);
            log.info("Ping result for CCTV {}: RTT: {}ms, PacketLoss: {}%", cctv.getCctvId(), pingCommandResult.getRtt(), pingCommandResult.getPacketLossRate());
        } catch (Exception e) {
            log.error("Error occurred while executing ping task for CCTV: {}", cctv.getCctvId(), e);
        }
    }

}
