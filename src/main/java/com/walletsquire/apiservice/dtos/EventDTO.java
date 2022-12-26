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
public class EventDTO {

    private Long id;
    private String name;
    private String description;
    private String endDatetimestamp;
    private String startDatetimestamp;
    private AddressDTO address;
    private CurrencyDTO currency;

}