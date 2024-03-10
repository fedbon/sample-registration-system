package ru.fedbon.exception;

public class RoleAlreadyExistsException extends ApiException {

    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}
