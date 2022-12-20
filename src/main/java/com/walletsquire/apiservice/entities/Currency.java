package com.walletsquire.apiservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "currency")
@Entity
public class Currency extends BaseEntity {

    private String name;
    private String code;
    private String country;
    private String symbol;

}