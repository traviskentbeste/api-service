package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.DebitorDTO;
import com.walletsquire.apiservice.entities.Debitor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring",
        uses = {
                DebitorMapperQualifier.class
        }
)
public interface DebitorMapper {


    Debitor toEntity(DebitorDTO DebitorDto);


    DebitorDTO toDto(Debitor Debitor);

}