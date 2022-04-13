
package com.en.remembrance.exceptions;

import com.en.remembrance.response.Response;

import static com.en.remembrance.response.Response.*;


public class InvalidAccessException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public InvalidAccessException() {
        this(Status.INVALID_ACCESS.getMessage());
    }

    public InvalidAccessException(String message) {
        super(Status.INVALID_ACCESS, message);
    }

}
