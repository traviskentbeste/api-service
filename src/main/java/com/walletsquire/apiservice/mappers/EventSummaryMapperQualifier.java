package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.entities.EventSummary;
import com.walletsquire.apiservice.services.EventSummaryService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventSummaryMapperQualifier {

    @Autowired
    private EventSummaryService EventSummaryService;

    @Named("longToEventSummary")
    public EventSummary longToEventSummary(Long value) {
//        System.out.println("longToEventSummary : -->" + value + "<--");
        if (value != null) {
            Optional<EventSummary> optional = EventSummaryService.getById(value);
            if (optional.isPresent()) {
//                System.out.println("is present");
                return optional.get();
            }
//            System.out.println("not present");
        }
        return null;
    }

}
