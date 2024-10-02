package com.kt.nms.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kt.nms.user.model.User;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder
        .getContext()
        .getAuthentication();

    if (authentication == null ||
        !authentication.isAuthenticated() ||
        authentication instanceof AnonymousAuthenticationToken) {
      return Optional.empty();
    }

    if (authentication.getPrincipal() instanceof User) {
      User userPrincipal = (User) authentication.getPrincipal();
      return Optional.ofNullable(userPrincipal.getUserId());
    }
    return Optional.empty();
  }
}
