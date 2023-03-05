package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.Debitor;
import com.walletsquire.apiservice.repositories.DebitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DebitorService {

    @Autowired
    private DebitorRepository DebitorRepository;

    public Debitor create(Debitor Debitor) {

        return DebitorRepository.save(Debitor);

    }

    public Optional<Debitor> getById(Long id) {

        return DebitorRepository.findById(id);

    }

    public List<Debitor> getAll() {

        return DebitorRepository.findAll();

    }

    public Debitor update(Debitor Debitor, Long id) {

        Optional<Debitor> DebitorOptional = DebitorRepository.findById(id);

        if (! DebitorOptional.isPresent()) {
            return null;
        }

        Debitor existingDebitor = DebitorOptional.get();

    
        return DebitorRepository.save(existingDebitor);

    }

    public void delete(Debitor Debitor) {

        Optional<Debitor> DebitorOptional = DebitorRepository.findById(Debitor.getId());

        if (DebitorOptional.isPresent()) {
            DebitorRepository.delete(DebitorOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<Debitor> DebitorOptional = DebitorRepository.findById(id);

        if (DebitorOptional.isPresent()) {
            DebitorRepository.delete(DebitorOptional.get());
        }

    }

}
