package com.en.remembrance.exceptions;

import com.en.remembrance.response.Response.Status;


public class StoryBookNotFoundException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public StoryBookNotFoundException() {
        this(Status.RECORD_NOT_FOUND.getMessage());
    }

    public StoryBookNotFoundException(String message) {
        super(Status.RECORD_NOT_FOUND, message);
    }
}
