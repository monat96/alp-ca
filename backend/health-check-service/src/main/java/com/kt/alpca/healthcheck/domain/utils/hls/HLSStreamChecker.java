package com.kt.alpca.healthcheck.domain.utils.hls;

import com.kt.alpca.healthcheck.domain.model.enums.HLSStatus;
import com.kt.alpca.healthcheck.domain.model.enums.ICMPStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HLSStreamChecker {
    private static final String REQUEST_METHOD = "HEAD";
    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int READ_TIMEOUT = 3000;
    private static final Random TEST_RANDOM = new Random();

    public static HLSStatus exec(String hlsAddress) {
//        HLSStreamChecker checker = new HLSStreamChecker();
//        try {
//            if (checker.isStreamAccessible(hlsAddress)) return HLSStatus.SUCCESS;
//            return HLSStatus.FAIL;
//        } catch (IOException | URISyntaxException | IllegalArgumentException e) {
//            return HLSStatus.ERROR;
//        }
        List<HLSStatus> weightedStatuses = new ArrayList<>();

        // Status.SUCCESS 7번 추가
        for (int i = 0; i < 7; i++) {
            weightedStatuses.add(HLSStatus.SUCCESS);
        }

        // 나머지 Status 값들 1번씩 추가
        weightedStatuses.add(HLSStatus.ERROR);
        weightedStatuses.add(HLSStatus.FAIL);

        // 리스트에서 무작위로 하나 선택하여 반환
        return weightedStatuses.get(TEST_RANDOM.nextInt(weightedStatuses.size()));
    }

    private boolean isStreamAccessible(String hlsAddress) throws IOException, URISyntaxException, IllegalArgumentException {
        URL url = new URI(hlsAddress).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(REQUEST_METHOD);
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        int responseCode = connection.getResponseCode();
        return (responseCode >= 200 && responseCode < 400);
    }
}

