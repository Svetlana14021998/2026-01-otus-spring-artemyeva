package org.example.enums;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN("ADMIN", "Администратор"),
    TEACHER("TEACHER", "Преподаватель"),
    USER("USER", "Пользователь");

    private final String code;
    private final String roleName;

    UserRole(String code, String roleName) {
        this.code = code;
        this.roleName = roleName;
    }
}
