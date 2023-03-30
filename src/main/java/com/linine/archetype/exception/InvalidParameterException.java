package com.linine.archetype.exception;

/**
 * @author Linine
 * @since 2023/3/29 14:31
 */
public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException() {
    }

    public InvalidParameterException(String message) {
        super(message);
    }
}
