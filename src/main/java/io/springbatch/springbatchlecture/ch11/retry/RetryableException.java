package io.springbatch.springbatchlecture.ch11.retry;

public class RetryableException extends RuntimeException {

    public RetryableException() {
    }

    public RetryableException(String s) {
        super(s);
    }
}
