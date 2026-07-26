package org.example.exception;

import lombok.Getter;

@Getter
public class PermissionException extends RuntimeException {

    private final String msgForUser;

    public PermissionException(String message, String msgForUser) {
        super(message);
        this.msgForUser = msgForUser;
    }
}
