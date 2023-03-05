package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.CreditorDTO;
import com.walletsquire.apiservice.entities.Creditor;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.CreditorMapper;
import com.walletsquire.apiservice.services.CreditorService;
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
public class CreditorController {
    public static final String endpoint = "/creditor";

    @Autowired
    private CreditorService CreditorService;

    @Autowired
    private CreditorMapper CreditorMapper;

    @PostMapping(value = endpoint)
    public ResponseEntity<CreditorDTO> create(@Valid @RequestBody CreditorDTO CreditorDto) {

        Creditor Creditor = CreditorMapper.toEntity(CreditorDto);
        Creditor = CreditorService.create(Creditor);
        CreditorDto = CreditorMapper.toDto(Creditor);
        return new ResponseEntity<>(CreditorDto , HttpStatus.CREATED);

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<CreditorDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<Creditor> CreditorOptional = CreditorService.getById(id);

        if (!CreditorOptional.isPresent()) {
            throw new EntityNotFoundException(Creditor.class, "id", id.toString());
        }

        CreditorDTO CreditorDto  = CreditorMapper.toDto(CreditorOptional.get());

        return new ResponseEntity<>(CreditorDto , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<CreditorDTO> getAll() {

        List<CreditorDTO> CreditorDtoList = CreditorService.getAll()
                .stream()
                .map(Creditor -> {
                    return CreditorMapper.toDto(Creditor);
                }).collect(Collectors.toList());

        return CreditorDtoList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<CreditorDTO> update(@RequestBody CreditorDTO CreditorDto , @PathVariable @Min(1) Long id) {

        Optional<Creditor> CreditorOptional = CreditorService.getById(id);

        if (!CreditorOptional.isPresent()) {
            throw new EntityNotFoundException(Creditor.class, "id", id.toString());
        }

        Creditor Creditor = CreditorMapper.toEntity(CreditorDto );
        Creditor = CreditorService.update(Creditor, id);
        CreditorDto  = CreditorMapper.toDto(Creditor);

        return new ResponseEntity<>(CreditorDto , HttpStatus.OK);

    }

    @DeleteMapping(endpoint + "/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {

        // Creditor not found by the id
        Optional<Creditor> CreditorOptional = CreditorService.getById(id);

        if (!CreditorOptional.isPresent()) {
            throw new EntityNotFoundException(Creditor.class, "id", id.toString());
        }

        // do the delete
        CreditorService.delete(id);

        // response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }

}