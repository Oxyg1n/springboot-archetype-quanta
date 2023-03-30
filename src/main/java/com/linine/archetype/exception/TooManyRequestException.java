package com.linine.archetype.exception;

/**
 * @author Linine
 * @since 2023/3/29 14:40
 */
public class TooManyRequestException extends RuntimeException {
    public TooManyRequestException() {
    }

    public TooManyRequestException(String message) {
        super(message);
    }
}
