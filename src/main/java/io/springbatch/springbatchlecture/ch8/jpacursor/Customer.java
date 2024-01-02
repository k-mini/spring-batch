package io.springbatch.springbatchlecture.ch8.jpacursor;

import lombok.Data;

import javax.persistence.*;

//@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String birthdate;
}
