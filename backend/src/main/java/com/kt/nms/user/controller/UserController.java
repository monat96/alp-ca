package com.kt.nms.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.nms.user.dto.ChangePasswordRequest;
import com.kt.nms.user.service.UserService;

import java.security.Principal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PatchMapping
  public ResponseEntity<?> changePassword(
      @RequestBody ChangePasswordRequest request,
      Principal connectedUser) {
    userService.changePassword(request, connectedUser);

    return ResponseEntity.ok().build();
  }
}
