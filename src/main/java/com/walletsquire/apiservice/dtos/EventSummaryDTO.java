package com.walletsquire.apiservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSummaryDTO {

    private String name;

    private String description;

    private BigDecimal total;

    private Integer activitiesCount;

    private List<ActivityDTO> activities = new ArrayList<>();

    private List<ActivitySummaryDebitorsDTO> debitors = new ArrayList<>();

}
