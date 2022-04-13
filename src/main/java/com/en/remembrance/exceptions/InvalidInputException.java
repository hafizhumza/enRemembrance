
package com.en.remembrance.exceptions;

import com.en.remembrance.response.Response;


public class InvalidInputException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public InvalidInputException() {
        this(Response.Status.INVALID_INPUT.getMessage());
    }

    public InvalidInputException(String message) {
        super(Response.Status.INVALID_INPUT, message);
    }
}
