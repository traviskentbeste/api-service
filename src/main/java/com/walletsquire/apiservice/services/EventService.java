package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.Address;
import com.walletsquire.apiservice.entities.Currency;
import com.walletsquire.apiservice.entities.Event;
import com.walletsquire.apiservice.repositories.AddressRepository;
import com.walletsquire.apiservice.repositories.CurrencyRepository;
import com.walletsquire.apiservice.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event create(Event event) {

        return eventRepository.save(event);
    }

    public Optional<Event> getById(Long id) {

        return eventRepository.findById(id);

    }

    public List<Event> getAll() {

        return eventRepository.findAll();

    }

    public Event update(Event event, Long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (! eventOptional.isPresent()) {
            return null;
        }

        Event existingEvent = eventOptional.get();

        if ( (event.getName() != null) && (! event.getName().isEmpty()) ) {
            existingEvent.setName(event.getName());
        }
        if ( (event.getDescription() != null) && (! event.getDescription().isEmpty()) ) {
            existingEvent.setDescription(event.getDescription());
        }
        if ( (event.getEndDatetimestamp() != null) ) {
            existingEvent.setEndDatetimestamp(event.getEndDatetimestamp());
        }
        if ( (event.getStartDatetimestamp() != null) ) {
            existingEvent.setStartDatetimestamp(event.getStartDatetimestamp());
        }
        if ( (event.getAddress() != null) ) {
            existingEvent.setAddress(event.getAddress());
        }
        if ( (event.getCurrency() != null) ) {
            existingEvent.setCurrency(event.getCurrency());
        }
    
        return eventRepository.save(existingEvent);

    }

    public void delete(Event event) {

        Optional<Event> eventOptional = eventRepository.findById(event.getId());

        if (eventOptional.isPresent()) {
            eventRepository.delete(eventOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isPresent()) {
            eventRepository.delete(eventOptional.get());
        }

    }

}
