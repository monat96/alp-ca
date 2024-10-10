package com.kt.alpca.healthcheck.application.service;

import com.kt.alpca.healthcheck.domain.event.CCTVRegisteredEvent;
import com.kt.alpca.healthcheck.domain.event.HealthCheckedEvent;
import com.kt.alpca.healthcheck.domain.model.HealthCheck;
import com.kt.alpca.healthcheck.domain.model.enums.HLSStatus;
import com.kt.alpca.healthcheck.domain.model.enums.ICMPStatus;
import com.kt.alpca.healthcheck.domain.utils.hls.HLSStreamChecker;
import com.kt.alpca.healthcheck.domain.utils.icmp.EchoReply;
import com.kt.alpca.healthcheck.domain.utils.icmp.EchoChecker;
import com.kt.alpca.healthcheck.infra.repository.HealthCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckService {
    private final HealthCheckRepository healthCheckRepository;
    private final StreamBridge streamBridge;

    public void performHealthCheck(CCTVRegisteredEvent event) {
        HLSStatus hlsstatus = HLSStreamChecker.exec(event.getHlsAddress());
        EchoReply echoReply = EchoChecker.exec(event.getIpAddress());

        HealthCheck healthCheck = HealthCheck.builder()
                .cctvId(event.getCctvId())
                .rttMax(echoReply.getRtt().getMax())
                .rttAvg(echoReply.getRtt().getAvg())
                .rttMin(echoReply.getRtt().getMin())
                .icmpStatus(echoReply.getStatus())
                .hlsStatus(hlsstatus)
                .build();


        HealthCheck savedHealthChecked = healthCheckRepository.save(healthCheck);

        HealthCheckedEvent hlsHealthCheckedEvent = HealthCheckedEvent.builder()
                .cctvId(savedHealthChecked.getCctvId())
                .healthCheckId(savedHealthChecked.getId())
                .healthCheckType("HLS")
                .issuedAt(savedHealthChecked.getCreatedAt())
                .status(savedHealthChecked.getHlsStatus().toString())
                .build();

        HealthCheckedEvent icmpHealthCheckedEvent = HealthCheckedEvent.builder()
                .cctvId(savedHealthChecked.getCctvId())
                .healthCheckId(savedHealthChecked.getId())
                .healthCheckType("ICMP")
                .issuedAt(savedHealthChecked.getCreatedAt())
                .status(savedHealthChecked.getIcmpStatus().toString())
                .build();

        if (hlsstatus.equals(HLSStatus.FAIL) || hlsstatus.equals(HLSStatus.ERROR)) {
            streamBridge.send("health-check-event-out", hlsHealthCheckedEvent);
        }

        if (echoReply.getStatus().equals(ICMPStatus.FAIL) ||
                echoReply.getStatus().equals(ICMPStatus.TIMEOUT)) {
            streamBridge.send("health-check-event-out", icmpHealthCheckedEvent);
        }


    }
}
