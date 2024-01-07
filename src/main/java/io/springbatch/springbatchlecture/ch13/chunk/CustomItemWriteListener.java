package io.springbatch.springbatchlecture.ch13.chunk;

import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class CustomItemWriteListener implements ItemWriteListener<String> {
    @Override
    public void beforeWrite(List<? extends String> items) {
        System.out.println(">> before Write");
    }

    @Override
    public void afterWrite(List<? extends String> items) {
        System.out.println(">> after Write");
    }

    @Override
    public void onWriteError(Exception exception, List<? extends String> items) {
        System.out.println(">> on Write Error");
    }
}
