package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.*;
import com.walletsquire.apiservice.entities.*;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.ActivityMapper;
import com.walletsquire.apiservice.mappers.EventMapper;
import com.walletsquire.apiservice.services.*;
import com.walletsquire.apiservice.utilities.ActivityUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private ActivityService activityService;
    @Autowired
    private PaidService paidService;
    @Autowired
    private PaidUsersService paidUsersService;

    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private ActivityMapper activityMapper;

    private void validateEvent(Event event) {

        // currency
        if (event.getCurrency() == null) {
            throw new EntityNotFoundException(Currency.class, "currency", "is null");
        }
        if (event.getCurrency().getId() == null) {
            throw new EntityNotFoundException(Currency.class, "id", "is null");
        }
        Optional<Currency> currencyOptional = currencyService.getById(event.getCurrency().getId());
        if (! currencyOptional.isPresent()) {
            throw new EntityNotFoundException(Currency.class, "currency", "not found");
        }
        event.setCurrency(currencyOptional.get());

        // address
        if (event.getAddress() == null) {
            throw new EntityNotFoundException(Address.class, "address", "is null");
        }
        if (event.getAddress().getId() == null) {
            throw new EntityNotFoundException(Address.class, "id", "is null");
        }
        Optional<Address> addressOptional = addressService.getById(event.getAddress().getId());
        if (! addressOptional.isPresent()) {
            throw new EntityNotFoundException(Address.class, "address", "not found");
        }
        event.setAddress(addressOptional.get());

    }

    @PostMapping(value = endpoint)
    public ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO eventDto) {

        Event event = eventMapper.toEntity(eventDto);
        event = eventService.create(event);
        validateEvent(event);
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

        if (eventDto.getAddress() == null) {
            throw new EntityNotFoundException(Address.class, "address", "object not found");
        }

        if (eventDto.getCurrency() == null) {
            throw new EntityNotFoundException(Currency.class, "currency", "object not found");
        }

        if (eventDto.getAddress().getId() == null) {
            throw new EntityNotFoundException(Address.class, "id", "is null");
        }

        if (eventDto.getCurrency().getId() == null) {
            throw new EntityNotFoundException(Currency.class, "id", "is null");
        }

        Optional<Address> addressOptional = addressService.getById(eventDto.getAddress().getId());

        if (!addressOptional.isPresent()) {
            throw new EntityNotFoundException(Address.class, "id", "not found");
        }

        Optional<Currency> currencyOptional = currencyService.getById(eventDto.getCurrency().getId());

        if (! currencyOptional.isPresent()) {
            throw new EntityNotFoundException(Currency.class, "id", "not found");
        }

        Event event = eventMapper.toEntity(eventDto );
        event = eventService.update(event, id);
        event.setAddress(addressOptional.get());
        event.setCurrency(currencyOptional.get());
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

    @GetMapping(endpoint + "/{id}/activities")
    public List<ActivityDTO> activities(@PathVariable @Min(1) Long id) {
        int debug = 0;

        Optional<Event> eventEntityOptional = eventService.getById(id);

        if (eventEntityOptional.isPresent()) {

            Event event = eventEntityOptional.get();
            if (debug == 1) {
                System.out.println("event : " + event);
            }

            return eventService.getAllActivities(event).stream().map(activity -> {
                return activityMapper.toDto(activity);
            }).collect(Collectors.toList());

        }

        return new ArrayList<>();
    }

    @GetMapping(endpoint + "/{id}/summary")
    public ResponseEntity<EventSummaryDTO> summary(@PathVariable @Min(1) Long id) {
        int debug = 1;

        EventSummaryDTO eventSummaryDTO = new EventSummaryDTO();

        Optional<Event> eventEntityOptional = eventService.getById(id);

        if (eventEntityOptional.isPresent()) {

            Event event = eventEntityOptional.get();
            if (debug == 1) {
                System.out.println("event : " + event);
            }

            eventSummaryDTO.setEvent(eventMapper.toDto(event));
            eventSummaryDTO.setAmount(BigDecimal.ZERO);
            eventSummaryDTO.setActivities(new ArrayList<>());

            List<ActivitySummaryDebitorsDTO> debitors = new ArrayList<>();

            int index = 1;
            for(Activity activity: event.getActivities()) {

                System.out.printf("\n\n%d\n", index++);

                // combine the debtors
                eventService.combineActivityDebitors(debitors, activityService.getDebitors(activity));

                // add the activity
                //eventSummaryDTO.getActivities().add(activityMapper.toDto(activity));

                // update the eventSummaryDTO.total
                eventSummaryDTO.setAmount(eventSummaryDTO.getAmount().add(activity.getAmount()));

            }

            eventSummaryDTO.setDebitors(debitors);

            return new ResponseEntity<>(eventSummaryDTO, HttpStatus.OK);
        }

        throw new EntityNotFoundException(Event.class, "id", id.toString());
    }

    @GetMapping(endpoint + "/{id}/recalculate")
    public ResponseEntity<EventDTO> recalcuate(@PathVariable @Min(1) Long id) {

        Optional<Event> eventEntityOptional = eventService.getById(id);

        if (eventEntityOptional.isPresent()) {
            Event eventEntity = eventEntityOptional.get();

            System.out.println(eventEntity.getActivities());
            for(Activity activityEntity : eventEntity.getActivities()) {

//                if (activityEntity.getId() == 24) {
//                    System.out.println("activityEntity : " + activityEntity);
                Paid paidBy = activityEntity.getPaidBy();
                //System.out.println("  paidBy before : " + paidBy);
                paidBy.setCalculatedSplitAmount(ActivityUtilities.calculatedSplitAmount(activityEntity.getAmount(), BigDecimal.valueOf(paidBy.getPaidUsers().size())));
                paidBy.setHasExtraPenny(ActivityUtilities.hasExtraPenny(activityEntity.getAmount(), BigDecimal.valueOf(paidBy.getPaidUsers().size())));
                paidService.update(paidBy, paidBy.getId());
                //System.out.println("  paidBy after  : " + paidBy);
                // update the amounts for the paidUsers
                for(PaidUsers paidUsersEntity : paidBy.getPaidUsers()) {
                    paidUsersEntity.setAmount(paidBy.getCalculatedSplitAmount());
                    paidUsersService.update(paidUsersEntity, paidUsersEntity.getId());
                }

                Paid paidFor = activityEntity.getPaidFor();
                //System.out.println("  paidFor before : " + paidFor);
                paidFor.setCalculatedSplitAmount(ActivityUtilities.calculatedSplitAmount(activityEntity.getAmount(), BigDecimal.valueOf(paidFor.getPaidUsers().size())));
                paidFor.setHasExtraPenny(ActivityUtilities.hasExtraPenny(activityEntity.getAmount(), BigDecimal.valueOf(paidFor.getPaidUsers().size())));
                paidService.update(paidFor, paidFor.getId());
                //System.out.println("  paidFor after  : " + paidFor);
                // update the amounts for the paidUsers
                for(PaidUsers paidUsersEntity : paidFor.getPaidUsers()) {
                    paidUsersEntity.setAmount(paidFor.getCalculatedSplitAmount());
                    paidUsersService.update(paidUsersEntity, paidUsersEntity.getId());
                }
//                }
            }

            EventDTO eventDTO = eventMapper.toDto(eventEntity);

            return new ResponseEntity<>(eventDTO , HttpStatus.OK);

        }

        throw new EntityNotFoundException(Event.class, "id", id.toString());

    }
}