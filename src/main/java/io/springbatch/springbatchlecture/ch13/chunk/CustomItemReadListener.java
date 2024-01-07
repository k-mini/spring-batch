package io.springbatch.springbatchlecture.ch13.chunk;

import org.springframework.batch.core.ItemReadListener;

public class CustomItemReadListener implements ItemReadListener<Integer> {
    @Override
    public void beforeRead() {
        System.out.println(">> before Read");
    }

    @Override
    public void afterRead(Integer item) {
        System.out.println(">> after Read");
    }

    @Override
    public void onReadError(Exception ex) {
        System.out.println(">> on Read Error");
    }
}
