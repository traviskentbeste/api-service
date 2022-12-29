package com.walletsquire.apiservice.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ActivitySummaryCreditorDTO {

    private UserDTO user;

    private BigDecimal amount;

}
