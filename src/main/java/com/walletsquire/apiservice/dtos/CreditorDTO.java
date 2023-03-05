package com.walletsquire.apiservice.dtos;

import com.walletsquire.apiservice.entities.Debitor;
import com.walletsquire.apiservice.entities.User;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditorDTO {

    private Long id;

    private BigDecimal amount;

    private UserDTO user;

}