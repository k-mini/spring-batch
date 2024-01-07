package io.springbatch.springbatchlecture.ch13.skipandretry;

public class CustomRetryException extends Exception {
    public CustomRetryException(String message) {
        super(message);
    }

    public CustomRetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
