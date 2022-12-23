package com.walletsquire.apiservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "user")
@Entity
public class User extends BaseEntity {

    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return "User{" +
                "id='" + this.getId() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                "}";
    }
}