package com.walletsquire.apiservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long id;
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