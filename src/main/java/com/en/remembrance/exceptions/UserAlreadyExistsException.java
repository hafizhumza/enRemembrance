package com.en.remembrance.exceptions;

import com.en.remembrance.response.Response.Status;


public class UserAlreadyExistsException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException() {
        this(Status.NOT_USER_EXCEPTION.getMessage());
    }

    public UserAlreadyExistsException(String message) {
        super(Status.NOT_USER_EXCEPTION, message);
    }
}
