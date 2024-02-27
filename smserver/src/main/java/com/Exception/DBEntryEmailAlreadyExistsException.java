package com.Exception;

public class DBEntryEmailAlreadyExistsException extends DBEntryAlreadyExistsException{
    public DBEntryEmailAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
