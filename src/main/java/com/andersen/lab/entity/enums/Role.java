package com.andersen.lab.entity.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.andersen.lab.entity.enums.Permission.*;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Role {
    USER(
            Set.of(
                    USER_READ
            )),
    EDITOR(
            Set.of(
                    EDITOR_CREATE,
                    EDITOR_UPDATE,
                    EDITOR_READ,
                    EDITOR_DELETE
            )

    ),
    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_UPDATE,
                    ADMIN_READ,
                    ADMIN_DELETE,
                    EDITOR_CREATE,
                    EDITOR_UPDATE,
                    EDITOR_READ,
                    EDITOR_DELETE,
                    USER_READ
            )
    );

    Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
