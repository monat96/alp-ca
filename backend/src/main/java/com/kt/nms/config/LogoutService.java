package com.kt.nms.config;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.kt.nms.token.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
  private static final String BEARER = "Bearer ";
  private static final int BEARER_LENGTH = BEARER.length();

  private final TokenRepository tokenRepository;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String jwt;

    if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
      return;
    }

    jwt = authorizationHeader.substring(BEARER_LENGTH);
    var storedToken = tokenRepository.findByToken(jwt).orElse(null);

    if (storedToken == null) {
      return;
    }

    storedToken.setExpired(true);
    storedToken.setRevoked(true);
    tokenRepository.save(storedToken);
    SecurityContextHolder.clearContext();
  }
}
