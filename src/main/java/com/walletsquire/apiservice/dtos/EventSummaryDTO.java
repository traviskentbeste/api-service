package com.walletsquire.apiservice.dtos;

import com.walletsquire.apiservice.entities.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSummaryDTO {

    private Long id;

    private BigDecimal amount;

    private EventDTO event;

    private List<ActivityDTO> activities = new ArrayList<>();

    private List<DebitorDTO> debitors = new ArrayList<>();

}