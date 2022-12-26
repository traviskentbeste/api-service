package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.AddressDTO;
import com.walletsquire.apiservice.dtos.CurrencyDTO;
import com.walletsquire.apiservice.dtos.EventDTO;
import com.walletsquire.apiservice.entities.Address;
import com.walletsquire.apiservice.entities.Currency;
import com.walletsquire.apiservice.entities.Event;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.EventMapper;
import com.walletsquire.apiservice.services.AddressService;
import com.walletsquire.apiservice.services.CurrencyService;
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
    private AddressService addressService;
    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private EventMapper eventMapper;

    @PostMapping(value = endpoint)
    public ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO eventDto) {

        // only create if address exists
        // only create if currency exits
        if (eventDto.getAddress() != null) {
            if (eventDto.getCurrency() != null) {
                if (eventDto.getAddress().getId() != null) {
                    if (eventDto.getCurrency().getId() != null) {
                        Optional<Address> addressOptional = addressService.getById(eventDto.getAddress().getId());
                        if (addressOptional.isPresent()) {
                            Optional<Currency> currencyOptional = currencyService.getById(eventDto.getCurrency().getId());
                            if (currencyOptional.isPresent()) {

                                Event event = eventMapper.toEntity(eventDto);
                                event = eventService.create(event);
                                event.setAddress(addressOptional.get());
                                event.setCurrency(currencyOptional.get());
                                eventDto = eventMapper.toDto(event);
                                return new ResponseEntity<>(eventDto , HttpStatus.CREATED);

                            }
                            throw new EntityNotFoundException(Currency.class, "id", "not found");
                        }
                        throw new EntityNotFoundException(Address.class, "id", "not found");
                    }
                    throw new EntityNotFoundException(Currency.class, "id", "is null");
                }
                throw new EntityNotFoundException(Address.class, "id", "is null");
            }
            throw new EntityNotFoundException(Currency.class, "currency", "object not found");
        }
        throw new EntityNotFoundException(Address.class, "address", "object not found");

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

        // only create if address exists
        // only create if currency exits
        if (eventDto.getAddress() != null) {
            if (eventDto.getCurrency() != null) {
                if (eventDto.getAddress().getId() != null) {
                    if (eventDto.getCurrency().getId() != null) {
                        Optional<Address> addressOptional = addressService.getById(eventDto.getAddress().getId());
                        if (addressOptional.isPresent()) {
                            Optional<Currency> currencyOptional = currencyService.getById(eventDto.getCurrency().getId());
                            if (currencyOptional.isPresent()) {

                                Event event = eventMapper.toEntity(eventDto );
                                event = eventService.update(event, id);
                                event.setAddress(addressOptional.get());
                                event.setCurrency(currencyOptional.get());
                                eventDto  = eventMapper.toDto(event);

                                return new ResponseEntity<>(eventDto , HttpStatus.OK);

                            }
                            throw new EntityNotFoundException(Currency.class, "id", "not found");
                        }
                        throw new EntityNotFoundException(Address.class, "id", "not found");
                    }
                    throw new EntityNotFoundException(Currency.class, "id", "is null");
                }
                throw new EntityNotFoundException(Address.class, "id", "is null");
            }
            throw new EntityNotFoundException(Currency.class, "currency", "object not found");
        }
        throw new EntityNotFoundException(Address.class, "address", "object not found");


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