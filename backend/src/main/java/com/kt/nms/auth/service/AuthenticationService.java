package com.kt.nms.auth.service;

import com.kt.nms.auth.dto.AuthenticationRequest;
import com.kt.nms.auth.dto.AuthenticationResponse;
import com.kt.nms.auth.dto.RegisterRequest;
import com.kt.nms.common.BaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.nms.config.JwtService;
import com.kt.nms.token.enums.TokenType;
import com.kt.nms.token.model.Token;
import com.kt.nms.token.repository.TokenRepository;
import com.kt.nms.user.enums.Role;
import com.kt.nms.user.model.User;
import com.kt.nms.user.repository.UserRepository;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private static final String BEARER = "Bearer ";
  private static final int BEARER_LENGTH = BEARER.length();

  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .userId(request.getUserId())
        .name(request.getName())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();

    var savedUser = userRepository.save(user);
    var jwtToken = jwtService.generateToken(savedUser);
    var refreshToken = jwtService.generateRefreshToken(savedUser);

    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();

  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUserId(),
            request.getPassword()));

    var user = userRepository.findByUserId(request.getUserId()).orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }

  public void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();

    tokenRepository.save(token);
  }

  public void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());

    if (validUserTokens.isEmpty())
      return;

    validUserTokens.forEach((token -> {
      token.setExpired(true);
      token.setRevoked(true);
    }));

    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userId;

    if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
      return;
    }

    refreshToken = authorizationHeader.substring(BEARER_LENGTH);
    userId = jwtService.extractUsername(refreshToken);

    if (userId == null)
      return;

    var user = this.userRepository
        .findByUserId(userId)
        .orElseThrow();

    if (!jwtService.isTokenValid(refreshToken, user))
      return;

    var accessToken = jwtService.generateToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, accessToken);

    var authenticationResponse = AuthenticationResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

    new ObjectMapper().writeValue(response.getOutputStream(), BaseResponse.builder()
        .status(HttpStatus.OK)
        .message("Success")
        .data(authenticationResponse)
        .build());
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) {
  
  }
}
