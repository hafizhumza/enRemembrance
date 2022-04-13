
package com.en.remembrance.exceptions;

import com.en.remembrance.response.Response;


public class LinkExpiredException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public LinkExpiredException() {
        this(Response.Status.INVALID_INPUT.getMessage());
    }

    public LinkExpiredException(String message) {
        super(Response.Status.INVALID_INPUT, message);
    }
}
