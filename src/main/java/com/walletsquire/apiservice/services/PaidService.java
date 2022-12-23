package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.Paid;
import com.walletsquire.apiservice.repositories.PaidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaidService {

    @Autowired
    private PaidRepository paidRepository;

    public Paid create(Paid paid) {

        return paidRepository.save(paid);

    }

    public Optional<Paid> getById(Long id) {

        return paidRepository.findById(id);

    }

    public List<Paid> getAll() {

        return paidRepository.findAll();

    }

    public Paid update(Paid paid, Long id) {

        Optional<Paid> paidOptional = paidRepository.findById(id);

        if (! paidOptional.isPresent()) {
            return null;
        }

        Paid existingPaid = paidOptional.get();

        if ( (paid.getCalculatedSplitAmount() != null) ) {
            existingPaid.setCalculatedSplitAmount(paid.getCalculatedSplitAmount());
        }
        if ( (paid.getHasExtraPenny() != null) ) {
            existingPaid.setHasExtraPenny(paid.getHasExtraPenny());
        }
        if ( (paid.getType() != null) && (! paid.getType().isEmpty()) ) {
            existingPaid.setType(paid.getType());
        }
    
        return paidRepository.save(existingPaid);

    }

    public void delete(Paid paid) {

        Optional<Paid> paidOptional = paidRepository.findById(paid.getId());

        if (paidOptional.isPresent()) {
            paidRepository.delete(paidOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<Paid> paidOptional = paidRepository.findById(id);

        if (paidOptional.isPresent()) {
            paidRepository.delete(paidOptional.get());
        }

    }

}
