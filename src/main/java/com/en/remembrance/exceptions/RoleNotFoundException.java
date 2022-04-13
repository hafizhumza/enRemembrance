package com.en.remembrance.exceptions;

import com.en.remembrance.response.Response.Status;


public class RoleNotFoundException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public RoleNotFoundException() {
        this(Status.RECORD_NOT_FOUND.getMessage());
    }

    public RoleNotFoundException(String message) {
        super(Status.RECORD_NOT_FOUND, message);
    }
}
