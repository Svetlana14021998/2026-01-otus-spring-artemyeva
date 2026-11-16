package org.example.exception;

public class CantSaveFileException extends RuntimeException {

    public CantSaveFileException(String message) {
        super(message);
    }

    public CantSaveFileException() {
        super("Не удалось сохранить файл");
    }
}
