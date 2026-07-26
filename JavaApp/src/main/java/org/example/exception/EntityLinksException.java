package org.example.exception;

import lombok.Getter;

@Getter
public class EntityLinksException extends RuntimeException {

    private final String msgForUser;

    public EntityLinksException(String message, String msgForUser) {
        super(message);
        this.msgForUser = msgForUser;
    }
}
