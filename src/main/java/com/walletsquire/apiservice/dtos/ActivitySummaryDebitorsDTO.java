package com.walletsquire.apiservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ActivitySummaryDebitorsDTO {

    private UserDTO user;

    private BigDecimal total;

    private List<ActivitySummaryCreditorDTO> creditors = new ArrayList<>();

    @JsonIgnore
    public void addCreditor(ActivitySummaryCreditorDTO creditor) {
        creditors.add(creditor);
    }

}
