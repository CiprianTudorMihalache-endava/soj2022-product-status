package com.endava.tmd.soj.ps.exception;

public class IncompleteUserPreferencesException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IncompleteUserPreferencesException(final String message) {
        super(message);
    }

}
