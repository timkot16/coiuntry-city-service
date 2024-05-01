package com.andersen.lab.entity.enums;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    EDITOR_READ("editor:read"),
    EDITOR_UPDATE("editor:update"),
    EDITOR_CREATE("editor:create"),
    EDITOR_DELETE("editor:delete"),
    USER_READ("user:read");

    String permission;

}
