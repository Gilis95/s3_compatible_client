package com.kiro.s3.compatible.filesystem.api.exceptions;

public class FileSystemOperationException extends Exception {

    public FileSystemOperationException(String message) {
        super(message);
    }

    public FileSystemOperationException(String message, Throwable t) {
        super(message, t);
    }

    public FileSystemOperationException(Throwable cause) {
        super(cause);
    }

}
