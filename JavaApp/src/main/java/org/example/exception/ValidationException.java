package org.example.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {

    private Map<String, String> validationResults;

    public ValidationException(String message, Map<String, String> validationResults) {
        super(message);
        this.validationResults = validationResults;
    }
}
