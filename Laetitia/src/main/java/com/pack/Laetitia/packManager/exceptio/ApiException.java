package com.pack.Laetitia.packManager.exceptio;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }

    public ApiException() {
        super("An Error occurred");
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
