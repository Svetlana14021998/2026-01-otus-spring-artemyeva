package ru.otus.hw.exception;

/**
 * сущность не найдена в базе
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
