package com.Exception;

public class DBEntryUsernameAlreadyExists extends DBEntryAlreadyExistsException{
    public DBEntryUsernameAlreadyExists(String errorMessage) {
        super(errorMessage);
    }
}