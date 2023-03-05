package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.entities.Debitor;
import com.walletsquire.apiservice.services.DebitorService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DebitorMapperQualifier {

    @Autowired
    private DebitorService DebitorService;

    @Named("longToDebitor")
    public Debitor longToDebitor(Long value) {
//        System.out.println("longToDebitor : -->" + value + "<--");
        if (value != null) {
            Optional<Debitor> optional = DebitorService.getById(value);
            if (optional.isPresent()) {
//                System.out.println("is present");
                return optional.get();
            }
//            System.out.println("not present");
        }
        return null;
    }

}
