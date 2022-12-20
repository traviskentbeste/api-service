package com.walletsquire.apiservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "address")
@Entity
public class Address extends BaseEntity {

    private String streetNumber;
    private String route;
    private String locality;
    private String subLocality;
    private String neighborhood;
    private String administrativeAreaLevel1;
    private String administrativeAreaLevel2;
    private String country;
    private String continent;
    private String postalCode;
    private Double latitude;
    private Double longitude;
    private String placeId;

}