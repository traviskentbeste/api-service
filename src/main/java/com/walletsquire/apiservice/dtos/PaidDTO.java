package com.walletsquire.apiservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaidDTO {

    private Long id;
    private BigDecimal calculatedSplitAmount;
    private Boolean hasExtraPenny;
    private String type;

//    @JsonIgnore
//    private Set<PaidUsersDTO> paidUsers = new HashSet<>();

}