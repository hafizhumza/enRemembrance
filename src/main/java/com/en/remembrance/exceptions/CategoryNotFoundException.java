package com.en.remembrance.exceptions;

import com.en.remembrance.response.Response.Status;


public class CategoryNotFoundException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public CategoryNotFoundException() {
        this(Status.RECORD_NOT_FOUND.getMessage());
    }

    public CategoryNotFoundException(String message) {
        super(Status.RECORD_NOT_FOUND, message);
    }
}
