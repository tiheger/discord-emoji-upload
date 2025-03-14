package de.tiheger.exception;

public class UploadException extends RuntimeException {
    public UploadException(Throwable cause) {
        super("Could not upload file", cause);
    }
}
