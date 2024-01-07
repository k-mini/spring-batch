package io.springbatch.springbatchlecture.ch13.skipandretry;

public class CustomSkipException extends Exception {
    public CustomSkipException(String message) {
        super(message);
    }

    public CustomSkipException(String message, Throwable cause) {
        super(message, cause);
    }
}
