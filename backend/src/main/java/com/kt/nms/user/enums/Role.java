package com.kt.nms.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.kt.nms.user.enums.Permission.ADMIN_CREATE;
import static com.kt.nms.user.enums.Permission.ADMIN_DELETE;
import static com.kt.nms.user.enums.Permission.ADMIN_READ;
import static com.kt.nms.user.enums.Permission.ADMIN_UPDATE;
import static com.kt.nms.user.enums.Permission.USER_READ;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role {
  USER(Set.of(USER_READ)),
  ADMIN(Set.of(ADMIN_READ, ADMIN_CREATE, ADMIN_UPDATE, ADMIN_DELETE));

  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
        .stream()
        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
        .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
