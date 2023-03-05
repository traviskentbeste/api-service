package com.walletsquire.apiservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walletsquire.apiservice.dtos.UserDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "creditor")
@Entity
public class Creditor extends BaseEntity {

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "debitor_id")
    private Debitor debitor;

}