package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.Creditor;
import com.walletsquire.apiservice.repositories.CreditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditorService {

    @Autowired
    private CreditorRepository CreditorRepository;

    public Creditor create(Creditor Creditor) {

        return CreditorRepository.save(Creditor);

    }

    public Optional<Creditor> getById(Long id) {

        return CreditorRepository.findById(id);

    }

    public List<Creditor> getAll() {

        return CreditorRepository.findAll();

    }

    public Creditor update(Creditor Creditor, Long id) {

        Optional<Creditor> CreditorOptional = CreditorRepository.findById(id);

        if (! CreditorOptional.isPresent()) {
            return null;
        }

        Creditor existingCreditor = CreditorOptional.get();

    
        return CreditorRepository.save(existingCreditor);

    }

    public void delete(Creditor Creditor) {

        Optional<Creditor> CreditorOptional = CreditorRepository.findById(Creditor.getId());

        if (CreditorOptional.isPresent()) {
            CreditorRepository.delete(CreditorOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<Creditor> CreditorOptional = CreditorRepository.findById(id);

        if (CreditorOptional.isPresent()) {
            CreditorRepository.delete(CreditorOptional.get());
        }

    }

}
