package com.Exception;

public class NoSuchDBEntryException extends Exception {
    public NoSuchDBEntryException(String errorMessage) {
        super(errorMessage);
    }
}
