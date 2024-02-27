package com.Exception;

public class DBEntryIDAlreadyExistsException extends DBEntryAlreadyExistsException{
    public DBEntryIDAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
