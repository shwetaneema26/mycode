package com.abc.wordcounter.exception;

public class InvalidWordException extends Exception {

    public InvalidWordException(String message) {
        super(message);
    }
    public InvalidWordException(Throwable cause) {
        super(cause);
    }
    public InvalidWordException(String message, Throwable cause) {
        super(message, cause);
    }
}