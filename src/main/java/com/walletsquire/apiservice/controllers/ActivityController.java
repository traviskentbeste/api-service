package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.ActivityDTO;
import com.walletsquire.apiservice.entities.*;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.ActivityMapper;
import com.walletsquire.apiservice.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{baseURI}/v{version}")
@Validated
public class ActivityController {
    public static final String endpoint = "/activity";

    @Autowired
    private ActivityService activityService;
    @Autowired
    private EventService eventService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PaidService paidService;

    @Autowired
    private ActivityMapper activityMapper;
    
    private void validateActivity(Activity activity) {
        // event
        if (activity.getEvent() == null) {
            throw new EntityNotFoundException(Event.class, "event", "is null");
        }
        if (activity.getEvent().getId() == null) {
            throw new EntityNotFoundException(Event.class, "id", "is null");
        }
        Optional<Event> eventOptional = eventService.getById(activity.getEvent().getId());
        if (! eventOptional.isPresent()) {
            throw new EntityNotFoundException(Event.class, "event", "not found");
        }
        activity.setEvent(eventOptional.get());

        // currency
        if (activity.getCurrency() == null) {
            throw new EntityNotFoundException(Currency.class, "currency", "is null");
        }
        if (activity.getCurrency().getId() == null) {
            throw new EntityNotFoundException(Currency.class, "id", "is null");
        }
        Optional<Currency> currencyOptional = currencyService.getById(activity.getCurrency().getId());
        if (! currencyOptional.isPresent()) {
            throw new EntityNotFoundException(Currency.class, "currency", "not found");
        }
        activity.setCurrency(currencyOptional.get());

        // address
        if (activity.getAddress() == null) {
            throw new EntityNotFoundException(Address.class, "address", "is null");
        }
        if (activity.getAddress().getId() == null) {
            throw new EntityNotFoundException(Address.class, "id", "is null");
        }
        Optional<Address> addressOptional = addressService.getById(activity.getAddress().getId());
        if (! addressOptional.isPresent()) {
            throw new EntityNotFoundException(Address.class, "address", "not found");
        }
        activity.setAddress(addressOptional.get());

        // category
        if (activity.getCategory() == null) {
            throw new EntityNotFoundException(Category.class, "category", "is null");
        }
        if (activity.getCategory().getId() == null) {
            throw new EntityNotFoundException(Category.class, "id", "is null");
        }
        Optional<Category> categoryOptional = categoryService.getById(activity.getCategory().getId());
        if (! categoryOptional.isPresent()) {
            throw new EntityNotFoundException(Category.class, "category", "not found");
        }
        activity.setCategory(categoryOptional.get());

        // paidBy
        if (activity.getPaidBy() == null) {
            throw new EntityNotFoundException(Paid.class, "paidBy", "is null");
        }
        if (activity.getPaidBy().getId() == null) {
            throw new EntityNotFoundException(Paid.class, "id", "is null");
        }
        Optional<Paid> paidByOptional = paidService.getById(activity.getPaidBy().getId());
        if (! paidByOptional.isPresent()) {
            throw new EntityNotFoundException(Paid.class, "paidBy", "not found");
        }
        activity.setPaidBy(paidByOptional.get());

        // paidFor
        if (activity.getPaidFor() == null) {
            throw new EntityNotFoundException(Paid.class, "paidFor", "is null");
        }
        if (activity.getPaidFor().getId() == null) {
            throw new EntityNotFoundException(Paid.class, "id", "is null");
        }
        Optional<Paid> paidForOptional = paidService.getById(activity.getPaidFor().getId());
        if (! paidForOptional.isPresent()) {
            throw new EntityNotFoundException(Paid.class, "paidFor", "not found");
        }
        activity.setPaidFor(paidForOptional.get());

    }

    @PostMapping(value = endpoint)
    public ResponseEntity<ActivityDTO> create(@Valid @RequestBody ActivityDTO activityDTO) {

        Activity activity = activityMapper.toEntity(activityDTO);

        // handles 'sub' objects and throw errors if it exists
        validateActivity(activity);

        activity = activityService.create(activity);
        activityDTO = activityMapper.toDto(activity);
        return new ResponseEntity<>(activityDTO , HttpStatus.CREATED);

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<ActivityDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<Activity> activityOptional = activityService.getById(id);

        if (!activityOptional.isPresent()) {
            throw new EntityNotFoundException(Activity.class, "id", id.toString());
        }

        ActivityDTO activityDTO  = activityMapper.toDto(activityOptional.get());

        return new ResponseEntity<>(activityDTO , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<ActivityDTO> getAll() {

        List<ActivityDTO> activityDTOList = activityService.getAll()
                .stream()
                .map(activity -> {
                    return activityMapper.toDto(activity);
                }).collect(Collectors.toList());

        return activityDTOList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<ActivityDTO> update(@RequestBody ActivityDTO activityDTO , @PathVariable @Min(1) Long id) {

        Optional<Activity> activityOptional = activityService.getById(id);

        if (!activityOptional.isPresent()) {
            throw new EntityNotFoundException(Activity.class, "id", id.toString());
        }

        Activity activity = activityMapper.toEntity(activityDTO);

        // handles 'sub' objects and throw errors if it exists
        validateActivity(activity);

        activity = activityService.update(activity, id);
        activityDTO  = activityMapper.toDto(activity);

        return new ResponseEntity<>(activityDTO , HttpStatus.OK);

    }

    @DeleteMapping(endpoint + "/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {

        // activity not found by the id
        Optional<Activity> activityOptional = activityService.getById(id);

        if (!activityOptional.isPresent()) {
            throw new EntityNotFoundException(Activity.class, "id", id.toString());
        }

        // do the delete
        activityService.delete(id);

        // response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }

}