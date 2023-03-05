package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.entities.Creditor;
import com.walletsquire.apiservice.services.CreditorService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreditorMapperQualifier {

    @Autowired
    private CreditorService CreditorService;

    @Named("longToCreditor")
    public Creditor longToCreditor(Long value) {
//        System.out.println("longToCreditor : -->" + value + "<--");
        if (value != null) {
            Optional<Creditor> optional = CreditorService.getById(value);
            if (optional.isPresent()) {
//                System.out.println("is present");
                return optional.get();
            }
//            System.out.println("not present");
        }
        return null;
    }

}
