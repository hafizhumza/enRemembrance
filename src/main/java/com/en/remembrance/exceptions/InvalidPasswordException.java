package com.en.remembrance.exceptions;

import com.en.remembrance.response.Response.Status;


public class InvalidPasswordException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        this(Status.RECORD_NOT_FOUND.getMessage());
    }

    public InvalidPasswordException(String message) {
        super(Status.RECORD_NOT_FOUND, message);
    }
}
