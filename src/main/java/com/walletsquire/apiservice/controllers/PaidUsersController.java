package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.PaidUsersDTO;
import com.walletsquire.apiservice.entities.*;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.PaidUsersMapper;
import com.walletsquire.apiservice.services.PaidService;
import com.walletsquire.apiservice.services.PaidUsersService;
import com.walletsquire.apiservice.services.UserService;
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
public class PaidUsersController {
    public static final String endpoint = "/paid-users";

    @Autowired
    private PaidUsersService paidUsersService;
    @Autowired
    private PaidService paidService;
    @Autowired
    private UserService userService;

    @Autowired
    private PaidUsersMapper paidUsersMapper;

    private void validatePaidUsers(PaidUsers paidUsers) {

        // paid
        if (paidUsers.getPaid() == null) {
            throw new EntityNotFoundException(Paid.class, "paid", "is null");
        }
        if (paidUsers.getPaid().getId() == null) {
            throw new EntityNotFoundException(Paid.class, "id", "is null");
        }
        Optional<Paid> paidOptional = paidService.getById(paidUsers.getPaid().getId());
        if (! paidOptional.isPresent()) {
            throw new EntityNotFoundException(Paid.class, "paid", "not found");
        }
        paidUsers.setPaid(paidOptional.get());


        // user
        if (paidUsers.getUser() == null) {
            throw new EntityNotFoundException(User.class, "user", "is null");
        }
        if (paidUsers.getPaid().getId() == null) {
            throw new EntityNotFoundException(User.class, "id", "is null");
        }
        Optional<User> userOptional = userService.getById(paidUsers.getUser().getId());
        if (! userOptional.isPresent()) {
            throw new EntityNotFoundException(User.class, "user", "not found");
        }
        paidUsers.setUser(userOptional.get());

    }

    @PostMapping(value = endpoint)
    public ResponseEntity<PaidUsersDTO> create(@Valid @RequestBody PaidUsersDTO paidUsersDto) {

        PaidUsers paidUsers = paidUsersMapper.toEntity(paidUsersDto);
        validatePaidUsers(paidUsers);
        paidUsers = paidUsersService.create(paidUsers);
        paidUsersDto = paidUsersMapper.toDtoWithoutPaid(paidUsers);

        return new ResponseEntity<>(paidUsersDto , HttpStatus.CREATED);

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<PaidUsersDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<PaidUsers> paidUsersOptional = paidUsersService.getById(id);

        if (!paidUsersOptional.isPresent()) {
            throw new EntityNotFoundException(PaidUsers.class, "id", id.toString());
        }

        PaidUsersDTO paidUsersDto  = paidUsersMapper.toDtoWithoutPaid(paidUsersOptional.get());

        return new ResponseEntity<>(paidUsersDto , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<PaidUsersDTO> getAll() {

        List<PaidUsersDTO> paidUsersDtoList = paidUsersService.getAll()
                .stream()
                .map(paidUsers -> {
                    return paidUsersMapper.toDtoWithoutPaid(paidUsers);
                }).collect(Collectors.toList());

        return paidUsersDtoList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<PaidUsersDTO> update(@RequestBody PaidUsersDTO paidUsersDTO, @PathVariable @Min(1) Long id) {

        Optional<PaidUsers> paidUsersOptional = paidUsersService.getById(id);
        if (paidUsersOptional != null) {

            if (!paidUsersOptional.isPresent()) {
                throw new EntityNotFoundException(PaidUsers.class, "id", id.toString());
            }

            PaidUsers paidUsers = paidUsersOptional.get();
            paidUsers = paidUsersService.update(paidUsers, paidUsersDTO, id);
            paidUsersDTO = paidUsersMapper.toDtoWithoutPaid(paidUsers);

            return new ResponseEntity<>(paidUsersDTO, HttpStatus.OK);
        }

        throw new EntityNotFoundException(PaidUsers.class, "id", id.toString());
    }

    @DeleteMapping(endpoint + "/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {

        // paidUsers not found by the id
        Optional<PaidUsers> paidUsersOptional = paidUsersService.getById(id);

        if (!paidUsersOptional.isPresent()) {
            throw new EntityNotFoundException(PaidUsers.class, "id", id.toString());
        }

        // do the delete
        paidUsersService.delete(id);

        // response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }

}