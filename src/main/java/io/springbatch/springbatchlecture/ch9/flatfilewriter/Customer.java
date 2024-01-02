package io.springbatch.springbatchlecture.ch9.flatfilewriter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {

    private long id;
    private String name;
    private int age;
}
