package org.writer.exception;

import java.io.IOException;

public class FileWritingException extends RuntimeException {
    public FileWritingException(String message, IOException e) {
        super(message, e);
    }
}
