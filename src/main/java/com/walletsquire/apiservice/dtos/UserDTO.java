package com.walletsquire.apiservice.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;

}