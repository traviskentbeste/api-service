package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.PaidDTO;
import com.walletsquire.apiservice.entities.Paid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PaidMapper {

    PaidMapper INSTANCE = Mappers.getMapper(PaidMapper.class);

    Paid toEntity(PaidDTO paidDto);

    PaidDTO toDto(Paid paid);

}
