package com.walletsquire.apiservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "event")
@Entity
public class Event extends BaseEntity {

    private String name;
    private String description;
    private Date endDatetimestamp;
    private Date startDatetimestamp;

    // addresses are created on the fly
    @OneToOne
    private Address address;

    // currencies cannot be created
    @OneToOne
    private Currency currency;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Activity> activities = new ArrayList<>();

}