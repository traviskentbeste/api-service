package com.walletsquire.apiservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "event_summary")
@Entity
public class EventSummary extends BaseEntity {

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany
    @JoinTable(
            name               = "event_summary_debitors",
            joinColumns        = { @JoinColumn(name = "event_summary_id") },
            inverseJoinColumns = { @JoinColumn(name = "debitor_id") }
    )
    private List<Debitor> debitors = new ArrayList<>();

    public List<Debitor> getDebitors() {
        return this.debitors;
    }
}