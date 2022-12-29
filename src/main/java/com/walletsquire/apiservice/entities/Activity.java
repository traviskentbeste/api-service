package com.walletsquire.apiservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "activity")
@Entity
public class Activity extends BaseEntity {

    private String name;

    private String description;

    private BigDecimal amount;

    private String splitType;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    // currencies cannot be created
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @OneToOne
    private Address address;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne
    private Paid paidBy;

    @OneToOne
    private Paid paidFor;

//    @Override
//    public String toString() {
//        return "Activity{" +
//                "id=" + this.getId() +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", amount=" + amount +
//                ", splitType='" + splitType + '\'' +
//                ", event=" + event.getId() +
//                ", currency=" + currency.getId() +
//                ", address=" + address.getId() +
//                ", category=" + category.getId() +
//                ", paidBy=" + paidBy.getId() +
//                ", paidFor=" + paidFor.getId() +
//                "} " + super.toString();
//    }
}