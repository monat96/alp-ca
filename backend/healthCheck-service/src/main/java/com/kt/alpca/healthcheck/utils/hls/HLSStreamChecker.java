package com.kt.alpca.healthcheck.utils.hls;

import com.kt.alpca.healthcheck.enums.HLSStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HLSStreamChecker {
    private static final String REQUEST_METHOD = "HEAD";
    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int READ_TIMEOUT = 3000;

    public static HLSStatus exec(String hlsAddress) {
        HLSStreamChecker checker = new HLSStreamChecker();
        try {
            if (checker.isStreamAccessible(hlsAddress)) return HLSStatus.SUCCESS;
            return HLSStatus.FAIL;
        } catch (IOException | URISyntaxException | IllegalArgumentException e) {
            return HLSStatus.ERROR;
        }
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

