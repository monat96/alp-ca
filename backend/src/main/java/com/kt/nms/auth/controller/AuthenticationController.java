package com.kt.nms.auth.controller;

import com.kt.nms.auth.dto.AuthenticationRequest;
import com.kt.nms.auth.dto.AuthenticationResponse;
import com.kt.nms.auth.service.AuthenticationService;
import com.kt.nms.auth.dto.RegisterRequest;
import com.kt.nms.common.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(
                BaseResponse.<AuthenticationResponse>builder()
                        .status(HttpStatus.OK)
                        .message("User registered successfully")
                        .data(authenticationService.register(request)).build());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(
                BaseResponse.<AuthenticationResponse>builder()
                        .status(HttpStatus.OK)
                        .message("User authenticated successfully")
                        .data(authenticationService.authenticate(request)).build());
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("/logout")
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        authenticationService.logout(request, response);
    }
}
