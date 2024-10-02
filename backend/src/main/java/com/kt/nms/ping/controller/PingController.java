package com.kt.nms.ping.controller;

import com.kt.nms.cctv.service.CCTVService;
import com.kt.nms.common.BaseResponse;
import com.kt.nms.ping.dto.PingDto;
import com.kt.nms.ping.dto.PingStatusResponse;
import com.kt.nms.ping.model.Ping;
import com.kt.nms.ping.service.PingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ping")
@RequiredArgsConstructor
public class PingController {
    private final PingService pingService;

//    requestParam 추가
    @GetMapping("/{cctvId}")
    public ResponseEntity<BaseResponse<List<PingDto>>> getPingByCctvId(
            @PathVariable String cctvId
//            @RequestParam String requestTime
    ) {
        List<PingDto> pingList = pingService.getPingByCctvId(cctvId);

        return ResponseEntity.ok(BaseResponse.<List<PingDto>>builder()
                .status(HttpStatus.OK)
                .message("Success")
                .data(pingList)
                .build());
    }

    @GetMapping("/status")
    public ResponseEntity<BaseResponse<PingStatusResponse>> getPingStatus() {
        return ResponseEntity.ok(BaseResponse.<PingStatusResponse>builder()
                .status(HttpStatus.OK)
                .message("Success")
                .data(pingService.getPingStatus())
                .build());
    }
}
