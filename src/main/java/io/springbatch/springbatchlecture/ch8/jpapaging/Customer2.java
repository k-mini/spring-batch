package io.springbatch.springbatchlecture.ch8.jpapaging;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Customer2 {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int age;

    @OneToOne(mappedBy = "customer")
    private Address address;
}
