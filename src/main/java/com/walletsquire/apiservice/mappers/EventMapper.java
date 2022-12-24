package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.EventDTO;
import com.walletsquire.apiservice.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    Event toEntity(EventDTO eventDto);
    
    EventDTO toDto(Event event);

}