package com.eduardo.api_park.exception;

public class UsernameUniqueViolationException extends RuntimeException {
    public UsernameUniqueViolationException(String message)  {
        super(message);
    }
}
