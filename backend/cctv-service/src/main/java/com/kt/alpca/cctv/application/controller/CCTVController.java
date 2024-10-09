package com.kt.alpca.cctv.application.controller;

import com.kt.alpca.cctv.application.dto.UploadRequest;
import com.kt.alpca.cctv.application.service.CCTVService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cctvs")
@RequiredArgsConstructor
public class CCTVController {
    private final CCTVService cctvService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(UploadRequest request) {
        try {
            if (request.getFile() == null) {
                return ResponseEntity.badRequest().build();
            }
            cctvService.upload(request.getFile());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
