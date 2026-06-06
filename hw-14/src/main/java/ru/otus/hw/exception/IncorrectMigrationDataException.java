package ru.otus.hw.exception;

/**
 * В таблице миграции нет записи для нужного id
 */
public class IncorrectMigrationDataException extends RuntimeException {

    public IncorrectMigrationDataException(String message) {
        super(message);
    }
}
