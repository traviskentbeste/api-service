package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.PaidDTO;
import com.walletsquire.apiservice.dtos.UserDTO;
import com.walletsquire.apiservice.entities.Paid;
import com.walletsquire.apiservice.entities.User;
import com.walletsquire.apiservice.services.PaidService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public interface PaidMapper {

    PaidMapper INSTANCE = Mappers.getMapper(PaidMapper.class);

    @Mappings({
//            @Mapping(target = "paidUsers", source = "paidDto.paidUsers", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
            @Mapping(target = "type", source = "paidDto.type", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    })
    Paid toEntity(PaidDTO paidDto);
    
    PaidDTO toDto(Paid paid);


}