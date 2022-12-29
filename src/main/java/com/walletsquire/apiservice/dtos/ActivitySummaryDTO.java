package com.walletsquire.apiservice.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ActivitySummaryDTO {

    private BigDecimal amount;

    private List<ActivitySummaryDebitorsDTO> debitors = new ArrayList<>();

}
