package com.kt.alpca.healthcheck;

import com.kt.alpca.healthcheck.enums.HLSStatus;
import com.kt.alpca.healthcheck.utils.hls.HLSStreamChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HLSStreamCheckerTests {
    static final String SUCCESS_HLS_ADDRESS = "http://210.99.70.120:1935/live/cctv001.stream/playlist.m3u8";
    static final String FAIL_HLS_ADDRESS = "http://example.com/invalid.m3u8";
    static final String ERROR_HLS_ADDRESS = "invalid-url";

    @Test
    void success() {
        HLSStatus status = HLSStreamChecker.exec(SUCCESS_HLS_ADDRESS);
        assertEquals(HLSStatus.SUCCESS, status);
    }

    @Test
    void fail() {
        HLSStatus status = HLSStreamChecker.exec(FAIL_HLS_ADDRESS);
        assertEquals(HLSStatus.FAIL, status);
    }

    @Test
    void error() {
        HLSStatus status = HLSStreamChecker.exec(ERROR_HLS_ADDRESS);
        assertEquals(HLSStatus.ERROR, status);
    }
}
