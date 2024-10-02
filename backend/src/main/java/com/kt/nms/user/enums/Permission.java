package com.kt.nms.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

  ADMIN_READ("admin:read"),
  ADMIN_UPDATE("admin:update"),
  ADMIN_DELETE("admin:delete"),
  ADMIN_CREATE("admin:create"),

  USER_READ("user:read");

  private final String permission;
}
