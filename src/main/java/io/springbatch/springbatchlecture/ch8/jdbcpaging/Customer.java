package io.springbatch.springbatchlecture.ch8.jdbcpaging;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class Customer {

    private Long id;
    private String firstName;
    private String lastName;
    private String birthdate;
}
