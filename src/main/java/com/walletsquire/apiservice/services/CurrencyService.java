package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.Currency;
import com.walletsquire.apiservice.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public Currency create(Currency currency) {

        return currencyRepository.save(currency);

    }

    public Optional<Currency> getById(Long id) {

        return currencyRepository.findById(id);

    }

    public List<Currency> getAll() {

        return currencyRepository.findAll();

    }

    public Currency update(Currency currency, Long id) {

        Optional<Currency> currencyOptional = currencyRepository.findById(id);

        if (! currencyOptional.isPresent()) {
            return null;
        }

        Currency existingCurrency = currencyOptional.get();

        if ( (currency.getName() != null) && (! currency.getName().isEmpty()) ) {
            existingCurrency.setName(currency.getName());
        }
        if ( (currency.getCode() != null) && (! currency.getCode().isEmpty()) ) {
            existingCurrency.setCode(currency.getCode());
        }
        if ( (currency.getCountry() != null) && (! currency.getCountry().isEmpty()) ) {
            existingCurrency.setCountry(currency.getCountry());
        }
        if ( (currency.getSymbol() != null) && (! currency.getSymbol().isEmpty()) ) {
            existingCurrency.setSymbol(currency.getSymbol());
        }
    
        return currencyRepository.save(existingCurrency);

    }

    public void delete(Currency currency) {

        Optional<Currency> currencyOptional = currencyRepository.findById(currency.getId());

        if (currencyOptional.isPresent()) {
            currencyRepository.delete(currencyOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<Currency> currencyOptional = currencyRepository.findById(id);

        if (currencyOptional.isPresent()) {
            currencyRepository.delete(currencyOptional.get());
        }

    }

}
