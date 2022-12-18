package com.walletsquire.apiservice.entities;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Health {

    @Size(min = 1, max = 64, message = "must be between 1 and 64 characters")
    private String status;

}