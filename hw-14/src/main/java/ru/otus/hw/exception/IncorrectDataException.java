package ru.otus.hw.exception;

/**
 * некорретные данные
 * Например, у книги нет автора
 */
public class IncorrectDataException extends RuntimeException {

    public IncorrectDataException(String message) {
        super(message);
    }
}
