package com.walletsquire.apiservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
    private String splitType;
    private EventDTO event;
    private CurrencyDTO currency;
    private AddressDTO address;
    private CategoryDTO category;
    private PaidDTO paidBy;
    private PaidDTO paidFor;

}