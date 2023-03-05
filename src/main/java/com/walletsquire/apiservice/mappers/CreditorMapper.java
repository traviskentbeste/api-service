package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.CreditorDTO;
import com.walletsquire.apiservice.entities.Creditor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring",
        uses = {
                CreditorMapperQualifier.class
        }
)
public interface CreditorMapper {


    Creditor toEntity(CreditorDTO CreditorDto);


    CreditorDTO toDto(Creditor Creditor);

}