package com.en.remembrance.exceptions;

import com.en.remembrance.response.Response.Status;


public class UserNotFoundException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        this(Status.RECORD_NOT_FOUND.getMessage());
    }

    public UserNotFoundException(String message) {
        super(Status.RECORD_NOT_FOUND, message);
    }
}
