package com.Exception;

public class DBEntryAlreadyExistsException extends Exception {
    public DBEntryAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
