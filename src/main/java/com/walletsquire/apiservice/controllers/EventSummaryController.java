package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.EventSummaryDTO;
import com.walletsquire.apiservice.entities.EventSummary;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.EventSummaryMapper;
import com.walletsquire.apiservice.services.EventSummaryService;
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
public class EventSummaryController {
    public static final String endpoint = "/event-summary";

    @Autowired
    private EventSummaryService EventSummaryService;

    @Autowired
    private EventSummaryMapper EventSummaryMapper;

    @PostMapping(value = endpoint)
    public ResponseEntity<EventSummaryDTO> create(@Valid @RequestBody EventSummaryDTO eventSummaryDto) {

        System.out.println(eventSummaryDto);
        EventSummary eventSummary = EventSummaryMapper.toEntity(eventSummaryDto);
        System.out.println(eventSummary);
        eventSummary = EventSummaryService.create(eventSummary);
        System.out.println(eventSummary);
        eventSummaryDto = EventSummaryMapper.toDto(eventSummary);
        System.out.println(eventSummaryDto);
        return new ResponseEntity<>(eventSummaryDto , HttpStatus.CREATED);

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<EventSummaryDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<EventSummary> EventSummaryOptional = EventSummaryService.getById(id);

        if (!EventSummaryOptional.isPresent()) {
            throw new EntityNotFoundException(EventSummary.class, "id", id.toString());
        }

        EventSummaryDTO EventSummaryDto  = EventSummaryMapper.toDto(EventSummaryOptional.get());

        return new ResponseEntity<>(EventSummaryDto , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<EventSummaryDTO> getAll() {

        List<EventSummaryDTO> EventSummaryDtoList = EventSummaryService.getAll()
                .stream()
                .map(EventSummary -> {
                    return EventSummaryMapper.toDto(EventSummary);
                }).collect(Collectors.toList());

        return EventSummaryDtoList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<EventSummaryDTO> update(@RequestBody EventSummaryDTO EventSummaryDto , @PathVariable @Min(1) Long id) {

        Optional<EventSummary> EventSummaryOptional = EventSummaryService.getById(id);

        if (!EventSummaryOptional.isPresent()) {
            throw new EntityNotFoundException(EventSummary.class, "id", id.toString());
        }

        EventSummary EventSummary = EventSummaryMapper.toEntity(EventSummaryDto );
        EventSummary = EventSummaryService.update(EventSummary, id);
        EventSummaryDto  = EventSummaryMapper.toDto(EventSummary);

        return new ResponseEntity<>(EventSummaryDto , HttpStatus.OK);

    }

    @DeleteMapping(endpoint + "/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {

        // EventSummary not found by the id
        Optional<EventSummary> EventSummaryOptional = EventSummaryService.getById(id);

        if (!EventSummaryOptional.isPresent()) {
            throw new EntityNotFoundException(EventSummary.class, "id", id.toString());
        }

        // do the delete
        EventSummaryService.delete(id);

        // response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }

}