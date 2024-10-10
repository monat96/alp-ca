package com.kt.alpca.healthcheck.domain.utils.icmp;

import com.kt.alpca.healthcheck.domain.model.enums.ICMPStatus;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EchoChecker {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String PING_COMMAND = "ping";
    private static final String PING_OPTION = OS.contains("win") ? "-n" : "-c";
    private static final String PING_COUNT = "4";

    private static final Pattern RTT_PATTERN_WINDOWS = Pattern.compile("최소 = ([0-9]+)ms, 최대 = ([0-9]+)ms, 평균 = ([0-9]+)ms");
    private static final Pattern PACKET_LOSS_PATTERN_WINDOWS = Pattern.compile("([0-9]+)% 손실");
    private static final Pattern RTT_PATTERN_UNIX = Pattern.compile("min/avg/max/([a-z.]+) = ([0-9.]+)/([0-9.]+)/([0-9.]+)/([0-9.]+) ms");
    private static final Pattern PACKET_LOSS_PATTERN_UNIX = Pattern.compile("([0-9.]+)% packet loss");

    private static final Random TEST_RANDOM = new Random();

    public static EchoReply exec(String ipAddressOrHostname) {
        CommandLine command = new CommandLine(PING_COMMAND);

        command.addArgument(PING_OPTION);
        command.addArgument(PING_COUNT);
        command.addArgument(ipAddressOrHostname);

        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

        executor.setStreamHandler(streamHandler);

        EchoChecker echoRequest = new EchoChecker();

        try {
            executor.execute(command);
            String output = outputStream.toString();
            // output에서 RTT와 패킷 손실율 파싱
            RTT rtt = OS.contains("win") ? echoRequest.extractRTTWindows(output) : echoRequest.extractRTTUnix(output);
            float packetLossRate = OS.contains("win") ? echoRequest.extractPacketLoss(output, PACKET_LOSS_PATTERN_WINDOWS) : echoRequest.extractPacketLoss(output, PACKET_LOSS_PATTERN_UNIX);
            ICMPStatus icmpStatus = echoRequest.extractICMPStatus(true, packetLossRate);
            return new EchoReply(rtt, packetLossRate, true, icmpStatus);
        } catch (IOException e) {
            return new EchoReply(RTT.builder().build(), 100.f, false, ICMPStatus.TIMEOUT); // 실패한 경우
        }
    }

    // Windows 시스템에서 RTT 파싱
    private RTT extractRTTWindows(String output) {
        float minRTT = 0.f;
        float avgRTT = 0.f;
        float maxRTT = 0.f;

        Matcher matcher = RTT_PATTERN_WINDOWS.matcher(output);

        if (matcher.find()) {
            minRTT = Float.parseFloat(matcher.group(1));
            avgRTT = Float.parseFloat(matcher.group(3));
            maxRTT = Float.parseFloat(matcher.group(2));
        }

        return RTT
                .builder()
                .min(minRTT)
                .avg(avgRTT)
                .max(maxRTT)
                .build();
    }

    // Windows 시스템에서 패킷 손실율 파싱


    // Unix/Linux 시스템에서 RTT 파싱
    private RTT extractRTTUnix(String output) {
        float minRTT = 0.f;
        float avgRTT = 0.f;
        float maxRTT = 0.f;

        Matcher matcher = RTT_PATTERN_UNIX.matcher(output);

        if (matcher.find()) {
            minRTT = Float.parseFloat(matcher.group(2));
            avgRTT = Float.parseFloat(matcher.group(3));
            maxRTT = Float.parseFloat(matcher.group(4));
        }

        return RTT
                .builder()
                .min(minRTT)
                .avg(avgRTT)
                .max(maxRTT)
                .build();
    }

    private  float extractPacketLoss(String output, Pattern packetLossPattern) {
        float packetLoss = 100.f;

        Matcher matcher = packetLossPattern.matcher(output);

        if (matcher.find()) {
            packetLoss = Float.parseFloat(matcher.group(1));
        }

        return packetLoss;
    }

    private ICMPStatus extractICMPStatus(boolean isReachable, float packetLossRate) {
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
