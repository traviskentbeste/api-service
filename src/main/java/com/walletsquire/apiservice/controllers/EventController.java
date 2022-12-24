package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.EventDTO;
import com.walletsquire.apiservice.entities.Event;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.EventMapper;
import com.walletsquire.apiservice.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{baseURI}/v{version}")
@Validated
public class EventController {
    public static final String endpoint = "/event";

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    @PostMapping(value = endpoint)
    public ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO eventDto) {

        Event event = eventMapper.toEntity(eventDto);
        event = eventService.create(event);
        eventDto = eventMapper.toDto(event);
        return new ResponseEntity<>(eventDto , HttpStatus.CREATED);

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<EventDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<Event> eventOptional = eventService.getById(id);

        if (!eventOptional.isPresent()) {
            throw new EntityNotFoundException(Event.class, "id", id.toString());
        }

        EventDTO eventDto  = eventMapper.toDto(eventOptional.get());

        return new ResponseEntity<>(eventDto , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<EventDTO> getAll() {

        List<EventDTO> eventDtoList = eventService.getAll()
                .stream()
                .map(event -> {
                    return eventMapper.toDto(event);
                }).collect(Collectors.toList());

        return eventDtoList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<EventDTO> update(@RequestBody EventDTO eventDto , @PathVariable @Min(1) Long id) {

        Optional<Event> eventOptional = eventService.getById(id);

        if (!eventOptional.isPresent()) {
            throw new EntityNotFoundException(Event.class, "id", id.toString());
        }

        Event event = eventMapper.toEntity(eventDto );
        event = eventService.update(event, id);
        eventDto  = eventMapper.toDto(event);

        return new ResponseEntity<>(eventDto , HttpStatus.OK);

    }

    @DeleteMapping(endpoint + "/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {

        // event not found by the id
        Optional<Event> eventOptional = eventService.getById(id);

        if (!eventOptional.isPresent()) {
            throw new EntityNotFoundException(Event.class, "id", id.toString());
        }

        // do the delete
        eventService.delete(id);

        // response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }

}