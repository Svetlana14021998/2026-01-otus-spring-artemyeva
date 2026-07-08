package ru.otus.hw.exceptions;

import lombok.Getter;

@Getter
public class LoginAlreadyExistsException extends RuntimeException {

    private final String username;

    public LoginAlreadyExistsException(String username) {
        super("User with username '" + username + "' already exists");
        this.username = username;
    }
}
