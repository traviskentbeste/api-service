package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.EventSummary;
import com.walletsquire.apiservice.repositories.EventSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventSummaryService {

    @Autowired
    private EventSummaryRepository EventSummaryRepository;

    public EventSummary create(EventSummary EventSummary) {

        return EventSummaryRepository.save(EventSummary);

    }

    public Optional<EventSummary> getById(Long id) {

        return EventSummaryRepository.findById(id);

    }

    public List<EventSummary> getAll() {

        return EventSummaryRepository.findAll();

    }

    public EventSummary update(EventSummary EventSummary, Long id) {

        Optional<EventSummary> EventSummaryOptional = EventSummaryRepository.findById(id);

        if (! EventSummaryOptional.isPresent()) {
            return null;
        }

        EventSummary existingEventSummary = EventSummaryOptional.get();

    
        return EventSummaryRepository.save(existingEventSummary);

    }

    public void delete(EventSummary EventSummary) {

        Optional<EventSummary> EventSummaryOptional = EventSummaryRepository.findById(EventSummary.getId());

        if (EventSummaryOptional.isPresent()) {
            EventSummaryRepository.delete(EventSummaryOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<EventSummary> EventSummaryOptional = EventSummaryRepository.findById(id);

        if (EventSummaryOptional.isPresent()) {
            EventSummaryRepository.delete(EventSummaryOptional.get());
        }

    }

}
