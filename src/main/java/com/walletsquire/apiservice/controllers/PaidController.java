package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.PaidDTO;
import com.walletsquire.apiservice.dtos.PaidUsersDTO;
import com.walletsquire.apiservice.entities.Paid;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.PaidMapper;
import com.walletsquire.apiservice.mappers.PaidUsersMapper;
import com.walletsquire.apiservice.services.PaidService;
import com.walletsquire.apiservice.services.PaidUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{baseURI}/v{version}")
@Validated
public class PaidController {
    public static final String endpoint = "/paid";

    @Autowired
    private PaidService paidService;
    @Autowired
    private PaidUsersService paidUsersService;

    @Autowired
    private PaidMapper paidMapper;
    @Autowired
    private PaidUsersMapper paidUsersMapper;

    @PostMapping(value = endpoint)
    public ResponseEntity<PaidDTO> create(@Valid @RequestBody PaidDTO paidDto) {

//        System.out.println("1.  paidDto : " + paidDto);
        Paid paid = paidMapper.toEntity(paidDto);
//        System.out.println("2.  paid    : " + paid);
        paid = paidService.create(paid);
//        System.out.println("3.  paid    : " + paid);
        paidDto = paidMapper.toDto(paid);
//        System.out.println("4.  paidDto : " + paidDto);
        return new ResponseEntity<>(paidDto , HttpStatus.CREATED);

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<PaidDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<Paid> paidOptional = paidService.getById(id);

        if (!paidOptional.isPresent()) {
            throw new EntityNotFoundException(Paid.class, "id", id.toString());
        }

        PaidDTO paidDto  = paidMapper.toDto(paidOptional.get());

        return new ResponseEntity<>(paidDto , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<PaidDTO> getAll() {

        List<PaidDTO> paidDtoList = paidService.getAll()
                .stream()
                .map(paid -> {
                    return paidMapper.toDto(paid);
                }).collect(Collectors.toList());

        return paidDtoList;

    }

    @GetMapping(value = endpoint + "/{id}/paid-users")
    public List<PaidUsersDTO> getPaidUsers(@PathVariable @Min(1) Long id) {

        Optional<Paid> paidOptional = paidService.getById(id);

        if (!paidOptional.isPresent()) {
            throw new EntityNotFoundException(Paid.class, "id", id.toString());
        }

        List<PaidUsersDTO> paidUsersDTOList = paidUsersService.getAllPaidUsers(paidOptional.get())
                .stream()
                .map(paidUsers -> {
                    return paidUsersMapper.toDto(paidUsers);
                }).collect(Collectors.toList());

        return paidUsersDTOList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<PaidDTO> update(@RequestBody PaidDTO paidDto , @PathVariable @Min(1) Long id) {

        Optional<Paid> paidOptional = paidService.getById(id);

        if (!paidOptional.isPresent()) {
            throw new EntityNotFoundException(Paid.class, "id", id.toString());
        }

        Paid paid = paidMapper.toEntity(paidDto );
        paid = paidService.update(paid, id);
        paidDto  = paidMapper.toDto(paid);

        return new ResponseEntity<>(paidDto , HttpStatus.OK);

    }

    @DeleteMapping(endpoint + "/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {

        // paid not found by the id
        Optional<Paid> paidOptional = paidService.getById(id);

        if (!paidOptional.isPresent()) {
            throw new EntityNotFoundException(Paid.class, "id", id.toString());
        }

        // do the delete
        paidService.delete(id);

        // response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }

}