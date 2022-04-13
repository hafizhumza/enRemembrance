package com.en.remembrance.exceptions;

import com.en.remembrance.response.Response.Status;


public class StoryBookDeletedException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public StoryBookDeletedException() {
        this(Status.RECORD_NOT_FOUND.getMessage());
    }

    public StoryBookDeletedException(String message) {
        super(Status.RECORD_NOT_FOUND, message);
    }
}
