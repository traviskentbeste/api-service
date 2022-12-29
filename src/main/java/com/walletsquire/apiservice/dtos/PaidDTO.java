package com.walletsquire.apiservice.dtos;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidDTO {

    private Long id;
    private BigDecimal calculatedSplitAmount;
    private Boolean hasExtraPenny;
    private String type;

}