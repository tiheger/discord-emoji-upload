package de.tiheger.exception;

public class DeletionException extends RuntimeException {
    public DeletionException(Throwable cause) {
        super("Could not delete file", cause);
    }
}
