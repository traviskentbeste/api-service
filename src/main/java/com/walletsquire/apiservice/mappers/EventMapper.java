package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.EventDTO;
import com.walletsquire.apiservice.entities.Address;
import com.walletsquire.apiservice.entities.Currency;
import com.walletsquire.apiservice.entities.Event;
import com.walletsquire.apiservice.services.AddressService;
import com.walletsquire.apiservice.services.CurrencyService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


@Mapper(
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public abstract class EventMapper {

    @Autowired
    AddressService addressService;
    @Autowired
    CurrencyService currencyService;

    @Mappings({
            @Mapping(target = "startDatetimestamp", source = "eventDto.startDatetimestamp", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "endDatetimestamp", source = "eventDto.endDatetimestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    public abstract Event toEntity(EventDTO eventDto);

    @Mappings({
            @Mapping(target = "startDatetimestamp", source = "event.startDatetimestamp", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "endDatetimestamp", source = "event.endDatetimestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    public abstract EventDTO toDto(Event event);

    public Long mapAddressToLong(Address address) {
        if (address != null) {
            return address.getId();
        }
        return null;
    }
    public Long mapCurrencyToLong(Currency currency) {
        if (currency != null) {
            return currency.getId();
        }
        return null;
    }

    public Address mapLongToAddress(Long value) {
        if (value != null) {
            Optional<Address> addressOptional = addressService.getById(value);
            if (addressOptional.isPresent()) {
                return addressOptional.get();
            }
        }
        return null;
    }

    public Currency mapCurrency(Long value) {
        if (value != null) {
            Optional<Currency> currencyOptional = currencyService.getById(value);
            if (currencyOptional.isPresent()) {
                return currencyOptional.get();
            }
        }
        return null;
    }
}