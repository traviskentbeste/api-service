package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.EventSummaryDTO;
import com.walletsquire.apiservice.entities.EventSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring",
        uses = {
                EventSummaryMapperQualifier.class
        }
)
public interface EventSummaryMapper {


    EventSummary toEntity(EventSummaryDTO EventSummaryDto);


    EventSummaryDTO toDto(EventSummary EventSummary);

}