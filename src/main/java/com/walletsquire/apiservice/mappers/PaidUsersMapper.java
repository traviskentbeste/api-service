package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.PaidDTO;
import com.walletsquire.apiservice.dtos.PaidUsersDTO;
import com.walletsquire.apiservice.dtos.UserDTO;
import com.walletsquire.apiservice.entities.Paid;
import com.walletsquire.apiservice.entities.PaidUsers;
import com.walletsquire.apiservice.entities.User;
import com.walletsquire.apiservice.services.PaidService;
import com.walletsquire.apiservice.services.UserService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Optional;

@Mapper(
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public abstract class PaidUsersMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    public abstract PaidUsers toEntity(PaidUsersDTO paidUsersDto);

    public abstract PaidUsersDTO toDto(PaidUsers paidUsers);

    public User mapUser(UserDTO obj) {
//        System.out.println("mapUser is    : " + obj.getId());
        Optional<User> optional = userService.getById(obj.getId());
        if (optional.isPresent()) {
//            System.out.println("returning : " + optional.get());
            return optional.get();
        }
        return null;
    }

    public UserDTO mapUserDTO(User obj) {
//        System.out.println("mapUserDTO is : " + obj);
        return userMapper.toDto(obj);
    }

}