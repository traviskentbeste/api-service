package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.AddressDTO;
import com.walletsquire.apiservice.entities.Address;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.AddressMapper;
import com.walletsquire.apiservice.services.AddressService;
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
public class AddressController {
    public static final String endpoint = "/address";

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressMapper addressMapper;

    @PostMapping(value = endpoint)
    public ResponseEntity<AddressDTO> create(@Valid @RequestBody AddressDTO addressDto) {

        Address address = addressMapper.toEntity(addressDto);
        address = addressService.create(address);
        addressDto = addressMapper.toDto(address);
        return new ResponseEntity<>(addressDto , HttpStatus.CREATED);

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<AddressDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<Address> addressOptional = addressService.getById(id);

        if (!addressOptional.isPresent()) {
            throw new EntityNotFoundException(Address.class, "id", id.toString());
        }

        AddressDTO addressDto  = addressMapper.toDto(addressOptional.get());

        return new ResponseEntity<>(addressDto , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<AddressDTO> getAll() {

        List<AddressDTO> addressDtoList = addressService.getAll()
                .stream()
                .map(address -> {
                    return addressMapper.toDto(address);
                }).collect(Collectors.toList());

        return addressDtoList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<AddressDTO> update(@RequestBody AddressDTO addressDto , @PathVariable @Min(1) Long id) {

        Optional<Address> addressOptional = addressService.getById(id);

        if (!addressOptional.isPresent()) {
            throw new EntityNotFoundException(Address.class, "id", id.toString());
        }

        Address address = addressMapper.toEntity(addressDto );
        address = addressService.update(address, id);
        addressDto  = addressMapper.toDto(address);

        return new ResponseEntity<>(addressDto , HttpStatus.OK);

    }

    @DeleteMapping(endpoint + "/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {

        // address not found by the id
        Optional<Address> addressOptional = addressService.getById(id);

        if (!addressOptional.isPresent()) {
            throw new EntityNotFoundException(Address.class, "id", id.toString());
        }

        // do the delete
        addressService.delete(id);

        // response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }

}