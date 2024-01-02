package io.springbatch.springbatchlecture.ch9.adapter;

public class CustomService<T> {

    public void customWrite(T item) {
        System.out.println("item = " + item);
    }
}
