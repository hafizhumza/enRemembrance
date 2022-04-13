package com.en.remembrance.exceptions;

import com.en.remembrance.response.Response.Status;


public class StoryBookExpiredException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public StoryBookExpiredException() {
        this(Status.RECORD_NOT_FOUND.getMessage());
    }

    public StoryBookExpiredException(String message) {
        super(Status.RECORD_NOT_FOUND, message);
    }
}
