package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.CurrencyDTO;
import com.walletsquire.apiservice.entities.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    Currency toEntity(CurrencyDTO currencyDto);
    
    CurrencyDTO toDto(Currency currency);

}