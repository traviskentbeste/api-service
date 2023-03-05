package com.walletsquire.apiservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walletsquire.apiservice.dtos.ActivitySummaryCreditorDTO;
import com.walletsquire.apiservice.dtos.UserDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "debitor")
@Entity
public class Debitor extends BaseEntity {

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "debitor", cascade = CascadeType.ALL)
    private List<Creditor> creditors = new ArrayList<>();

//    @ManyToOne(
//            fetch = FetchType.EAGER,
//            optional = true,
//            cascade = CascadeType.PERSIST
//    )
//    @JoinTable(
//            name               = "event_summary_debitors",
//            joinColumns        = @JoinColumn(name = "event_summary_id", referencedColumnName = "id", nullable = true),
//            inverseJoinColumns = @JoinColumn(name = "debitor_id",       referencedColumnName = "id", nullable = true)
//    )
//    @JsonIgnoreProperties("debitors")
//    private EventSummary eventSummary;

}