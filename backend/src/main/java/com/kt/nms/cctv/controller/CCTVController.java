package com.kt.nms.cctv.controller;

import com.kt.nms.cctv.dto.CCTVDto;
import com.kt.nms.cctv.dto.CreateRequest;
import com.kt.nms.cctv.dto.UploadRequest;
import com.kt.nms.cctv.model.CCTV;
import com.kt.nms.cctv.service.CCTVService;
import com.kt.nms.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cctv")
@RequiredArgsConstructor
public class CCTVController {
    private final CCTVService cctvService;

    @PostMapping
    public ResponseEntity<BaseResponse<CCTV>> create(
            @RequestBody CreateRequest request
    ) {
        CCTV cctv = cctvService.create(request);

        return ResponseEntity.ok(
                BaseResponse.<CCTV>builder()
                        .status(HttpStatus.OK)
                        .message("Success")
                        .data(cctv)
                        .build());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<CCTVDto>>> list() {
        List<CCTVDto> cctvList = cctvService.list();

        return ResponseEntity.ok(
                BaseResponse.<List<CCTVDto>>builder()
                        .status(HttpStatus.OK)
                        .message("Success")
                        .data(cctvList)
                        .build()
        );


    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CCTVDto>> detail(
            @PathVariable String id) {
        CCTVDto cctv = cctvService.detail(id);

        return ResponseEntity.ok(
                BaseResponse.<CCTVDto>builder()
                        .status(HttpStatus.OK)
                        .message("Success")
                        .data(cctv)
                        .build()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestBody UploadRequest request,
            @PathVariable String id) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> delete(
            @PathVariable String id
    ) {
        cctvService.delete(id);

        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK)
                        .message("Success")
                        .build()
        );
    }

    @PostMapping("/upload")
    public ResponseEntity<BaseResponse<?>> uploadCSV(
            UploadRequest request
    ) {
        try {
            List<CCTV> cctvList = cctvService.uploadCSV(request.getFile());
            return ResponseEntity.ok(
                    BaseResponse.builder()
                            .status(HttpStatus.OK)
                            .message("Success")
                            .build()
            );
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }
}
