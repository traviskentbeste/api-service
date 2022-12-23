package com.walletsquire.apiservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "paidUsers")
@Entity
public class PaidUsers extends BaseEntity {

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "paid_id")
    private Paid paid;

    @Override
    public String toString() {
        return "PaidUsers{" +
                "id=" + amount +
                ", amount=" + amount +
                ", user=" + user +
                ", paid=" + paid +
                "} ";
    }
}