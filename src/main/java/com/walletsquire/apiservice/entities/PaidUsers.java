package com.walletsquire.apiservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
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

        String str = "PaidUsers{" +
                "id=" + this.getId() +
                ", amount=" + amount;
        if (paid != null) {
            str += ", paid.id=" + paid.getId();
        } else {
            str += ", paid=null";
        }
        if (user != null) {
            str += ", user.id=" + user.getId();
        } else {
            str += ", user=null";
        }
        str += "} ";

        return str;
    }
}