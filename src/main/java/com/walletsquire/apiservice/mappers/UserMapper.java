package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.UserDTO;
import com.walletsquire.apiservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserDTO userDto);
    
    UserDTO toDto(User user);

}