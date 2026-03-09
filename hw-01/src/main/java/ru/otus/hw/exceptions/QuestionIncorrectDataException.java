package ru.otus.hw.exceptions;

public class QuestionIncorrectDataException extends RuntimeException {
    public QuestionIncorrectDataException(String message) {
        super(message);
    }

    public QuestionIncorrectDataException(String message, Throwable ex) {
        super(message, ex);
    }
}
