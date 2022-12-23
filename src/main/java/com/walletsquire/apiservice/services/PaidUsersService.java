package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.dtos.PaidUsersDTO;
import com.walletsquire.apiservice.entities.Paid;
import com.walletsquire.apiservice.entities.PaidUsers;
import com.walletsquire.apiservice.entities.User;
import com.walletsquire.apiservice.repositories.PaidUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaidUsersService {

    @Autowired
    private PaidUsersRepository paidUsersRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private PaidService paidService;

    public PaidUsers create(PaidUsers paidUsers) {

        return paidUsersRepository.save(paidUsers);

    }

    public Optional<PaidUsers> getById(Long id) {

        return paidUsersRepository.findById(id);

    }

    public List<PaidUsers> getAll() {

        return paidUsersRepository.findAll();

    }

    public PaidUsers update(PaidUsers paidUsers, PaidUsersDTO paidUsersDTO, Long id) {

        int update = 0;

        if (! paidUsers.getUser().getId().equals(paidUsersDTO.getUser().getId())) {
            Optional<User> userOptional = userService.getById(paidUsersDTO.getUser().getId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                paidUsers.setUser(user);
                update++;
            }
        }

        if (! paidUsers.getPaid().getId().equals(paidUsersDTO.getPaid().getId())) {
            Optional<Paid> paidOptional = paidService.getById(paidUsersDTO.getPaid().getId());
            if (paidOptional.isPresent()) {
                paidUsers.setPaid(paidOptional.get());
                update++;
            }
        }

        if (! paidUsers.getAmount().equals(paidUsersDTO.getAmount())) {
            paidUsers.setAmount(paidUsersDTO.getAmount());
            update++;
        }

        if (update > 0) {
            update(paidUsers, id);
        }

        return paidUsers;

    }

    public PaidUsers update(PaidUsers paidUsers, Long id) {

        Optional<PaidUsers> paidUsersOptional = paidUsersRepository.findById(id);

        if (! paidUsersOptional.isPresent()) {
            return null;
        }

        PaidUsers existingPaidUsers = paidUsersOptional.get();
        if ( (paidUsers.getAmount() != null) ) {
            existingPaidUsers.setAmount(paidUsers.getAmount());
        }

        if ( (paidUsers.getUser() != null) ) {
            existingPaidUsers.setUser(paidUsers.getUser());
        }

        return paidUsersRepository.save(existingPaidUsers);

    }

    public void delete(PaidUsers paidUsers) {

        Optional<PaidUsers> paidUsersOptional = paidUsersRepository.findById(paidUsers.getId());

        if (paidUsersOptional.isPresent()) {
            paidUsersRepository.delete(paidUsersOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<PaidUsers> paidUsersOptional = paidUsersRepository.findById(id);

        if (paidUsersOptional.isPresent()) {
            paidUsersRepository.delete(paidUsersOptional.get());
        }

    }

}
