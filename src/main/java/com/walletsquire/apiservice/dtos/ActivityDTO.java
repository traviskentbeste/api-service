package com.walletsquire.apiservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
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