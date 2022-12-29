package com.walletsquire.apiservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "`user`")
@Entity
public class User extends BaseEntity {

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "user")
    private Set<PaidUsers> paidUsers = new HashSet<>();

}