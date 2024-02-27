package com.Exception;

public class DBEntryNameAlreadyExistsException extends DBEntryAlreadyExistsException {
    public DBEntryNameAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
