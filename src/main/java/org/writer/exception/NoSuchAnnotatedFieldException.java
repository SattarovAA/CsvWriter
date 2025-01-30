package org.writer.exception;

public class NoSuchAnnotatedFieldException extends RuntimeException {
    public NoSuchAnnotatedFieldException(String message) {
        super(message);
    }
}
