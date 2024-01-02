package io.springbatch.springbatchlecture.ch9.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer3 {

    @Id
    private long id;
    private String firstName;
    private String lastName;
    private Date birthdate;
}
