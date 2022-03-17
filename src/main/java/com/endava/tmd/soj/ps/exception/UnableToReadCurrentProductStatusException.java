package com.endava.tmd.soj.ps.exception;

public class UnableToReadCurrentProductStatusException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnableToReadCurrentProductStatusException(final String message) {
        super(message);
    }

    public UnableToReadCurrentProductStatusException(final Throwable cause) {
        super(cause);
    }

}
