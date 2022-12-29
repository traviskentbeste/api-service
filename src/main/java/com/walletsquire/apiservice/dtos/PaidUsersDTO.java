package com.walletsquire.apiservice.dtos;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaidUsersDTO {

    private Long id;

    private BigDecimal amount;

    private PaidDTO paid;
    private UserDTO user;

    @Override
    public String toString() {

        String str = "PaidUsersDTO{" +
                "id=" + id +
                ", amount=" + amount;
        if (paid != null) {
            str += ", paid=" + paid.getId();
        } else {
            str += ", paid=null";
        }
        if (user != null) {
            str += ", user=" + user.getId();
        } else {
            str += ", user=null";
        }
        str += '}';
        return str;
    }
}