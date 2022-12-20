package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.CurrencyDTO;
import com.walletsquire.apiservice.entities.Currency;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.CurrencyMapper;
import com.walletsquire.apiservice.services.CurrencyService;
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
public class CurrencyController {
    public static final String endpoint = "/currency";

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyMapper currencyMapper;

    @PostMapping(value = endpoint)
    public ResponseEntity<CurrencyDTO> create(@Valid @RequestBody CurrencyDTO currencyDto) {

        Currency currency = currencyMapper.toEntity(currencyDto);
        currency = currencyService.create(currency);
        currencyDto = currencyMapper.toDto(currency);
        return new ResponseEntity<>(currencyDto , HttpStatus.CREATED);

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<CurrencyDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<Currency> currencyOptional = currencyService.getById(id);

        if (!currencyOptional.isPresent()) {
            throw new EntityNotFoundException(Currency.class, "id", id.toString());
        }

        CurrencyDTO currencyDto  = currencyMapper.toDto(currencyOptional.get());

        return new ResponseEntity<>(currencyDto , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<CurrencyDTO> getAll() {

        List<CurrencyDTO> currencyDtoList = currencyService.getAll()
                .stream()
                .map(currency -> {
                    return currencyMapper.toDto(currency);
                }).collect(Collectors.toList());

        return currencyDtoList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<CurrencyDTO> update(@RequestBody CurrencyDTO currencyDto , @PathVariable @Min(1) Long id) {

        Optional<Currency> currencyOptional = currencyService.getById(id);

        if (!currencyOptional.isPresent()) {
            throw new EntityNotFoundException(Currency.class, "id", id.toString());
        }

        Currency currency = currencyMapper.toEntity(currencyDto );
        currency = currencyService.update(currency, id);
        currencyDto  = currencyMapper.toDto(currency);

        return new ResponseEntity<>(currencyDto , HttpStatus.OK);

    }

    @DeleteMapping(endpoint + "/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {

        // currency not found by the id
        Optional<Currency> currencyOptional = currencyService.getById(id);

        if (!currencyOptional.isPresent()) {
            throw new EntityNotFoundException(Currency.class, "id", id.toString());
        }

        // do the delete
        currencyService.delete(id);

        // response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }

}