package io.springbatch.springbatchlecture.ch8.itemreaderadapter;

public class CustomService<T> {

    private int cnt = 0;

    public T customRead() {

        return (T) ("item" + cnt++);
    }

}
