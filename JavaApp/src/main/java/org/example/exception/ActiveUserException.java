package org.example.exception;

public class ActiveUserException extends RuntimeException {

    public ActiveUserException(String message) {
        super(message);
    }
}
