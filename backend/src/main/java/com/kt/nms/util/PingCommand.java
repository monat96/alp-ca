package com.kt.nms.util;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingCommand {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String PING_COMMAND = "ping";
    private static final String PING_OPTION = OS.contains("win") ? "-n" : "-c";
    private static final String PING_COUNT = "4";

    public static final Pattern RTT_PATTERN_WINDOWS = Pattern.compile("최소 = ([0-9]+)ms, 최대 = ([0-9]+)ms, 평균 = ([0-9]+)ms");
    public static final Pattern PACKET_LOSS_PATTERN_WINDOWS = Pattern.compile("([0-9]+)% 손실");
    public static final Pattern RTT_PATTERN_UNIX = Pattern.compile("min/avg/max/([a-z.]+) = ([0-9.]+)/([0-9.]+)/([0-9.]+)/([0-9.]+) ms");
    public static final Pattern PACKET_LOSS_PATTERN_UNIX = Pattern.compile("([0-9]+)% packet loss");


    public static PingCommandResult exec(String ipAddressOrHostname) {
        CommandLine command = new CommandLine(PING_COMMAND);

        command.addArgument(PING_OPTION);
        command.addArgument(PING_COUNT);
        command.addArgument(ipAddressOrHostname);

        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

        executor.setStreamHandler(streamHandler);

        try {
            executor.execute(command);
            String output = outputStream.toString();
            // output에서 RTT와 패킷 손실율 파싱
            RTT rtt = OS.contains("win") ? extractRTTWindows(output) : extractRTTUnix(output);
            float packetLoss = OS.contains("win") ? extractPacketLoss(output, PACKET_LOSS_PATTERN_WINDOWS) : extractPacketLoss(output, PACKET_LOSS_PATTERN_UNIX);

            return new PingCommandResult(rtt, packetLoss, true);
        } catch (IOException e) {
            return new PingCommandResult(RTT.builder().build(), 100.f, false); // 실패한 경우
        }
    }

    // Windows 시스템에서 RTT 파싱
    private static RTT extractRTTWindows(String output) {
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
    private static RTT extractRTTUnix(String output) {
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

    private static float extractPacketLoss(String output, Pattern packetLossPattern) {
        float packetLoss = 100.f;

        Matcher matcher = packetLossPattern.matcher(output);

        if (matcher.find()) {
            packetLoss = Float.parseFloat(matcher.group(1));
        }

        return packetLoss;
    }
}
