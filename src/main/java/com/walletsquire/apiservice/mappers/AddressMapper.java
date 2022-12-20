package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.AddressDTO;
import com.walletsquire.apiservice.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address toEntity(AddressDTO addressDto);
    
    AddressDTO toDto(Address address);

}