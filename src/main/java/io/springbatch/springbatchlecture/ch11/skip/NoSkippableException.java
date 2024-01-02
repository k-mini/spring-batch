package io.springbatch.springbatchlecture.ch11.skip;

public class NoSkippableException extends Exception {
    public NoSkippableException(String s) {
        super(s);
    }
}
