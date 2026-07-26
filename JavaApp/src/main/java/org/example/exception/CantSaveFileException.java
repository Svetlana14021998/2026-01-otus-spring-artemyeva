package org.example.exception;

public class CantSaveFileException extends RuntimeException {

    public CantSaveFileException(String message) {
        super(message);
    }

    public CantSaveFileException() {
        super("Can`t save file");
    }
}
