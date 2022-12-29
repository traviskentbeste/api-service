package com.walletsquire.apiservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "paid")
@Entity
public class Paid extends BaseEntity {

    private BigDecimal calculatedSplitAmount;
    private Boolean hasExtraPenny;
    private String type;

    @OneToMany(mappedBy = "paid", cascade=CascadeType.ALL, orphanRemoval=true)
    private Set<PaidUsers> paidUsers = new HashSet<>();

    @Override
    public String toString() {
        return "Paid{" +
                "id='" + this.getId() + '\'' +
                ", calculatedSplitAmount=" + calculatedSplitAmount +
                ", hasExtraPenny=" + hasExtraPenny +
                ", type='" + type + '\'' +
                ", paidUsers.size()=" + paidUsers.size() +
                "} " + super.toString();
    }
}