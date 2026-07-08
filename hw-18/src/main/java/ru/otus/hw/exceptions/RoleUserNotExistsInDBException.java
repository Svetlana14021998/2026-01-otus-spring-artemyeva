package ru.otus.hw.exceptions;

public class RoleUserNotExistsInDBException extends RuntimeException {

    public RoleUserNotExistsInDBException() {
        super("Role USER not exists in DB!");
    }
}
