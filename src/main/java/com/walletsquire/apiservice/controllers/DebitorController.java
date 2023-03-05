package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.DebitorDTO;
import com.walletsquire.apiservice.entities.Debitor;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.DebitorMapper;
import com.walletsquire.apiservice.services.DebitorService;
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
public class DebitorController {
    public static final String endpoint = "/debitor";

    @Autowired
    private DebitorService DebitorService;

    @Autowired
    private DebitorMapper DebitorMapper;

    @PostMapping(value = endpoint)
    public ResponseEntity<DebitorDTO> create(@Valid @RequestBody DebitorDTO debitorDto) {

        System.out.println(debitorDto);
        Debitor debitor = DebitorMapper.toEntity(debitorDto);
        System.out.println(debitor);
        debitor = DebitorService.create(debitor);
        System.out.println(debitor);
        debitorDto = DebitorMapper.toDto(debitor);
        System.out.println(debitorDto);
        return new ResponseEntity<>(debitorDto , HttpStatus.CREATED);

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<DebitorDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<Debitor> DebitorOptional = DebitorService.getById(id);

        if (!DebitorOptional.isPresent()) {
            throw new EntityNotFoundException(Debitor.class, "id", id.toString());
        }

        DebitorDTO DebitorDto  = DebitorMapper.toDto(DebitorOptional.get());

        return new ResponseEntity<>(DebitorDto , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<DebitorDTO> getAll() {

        List<DebitorDTO> DebitorDtoList = DebitorService.getAll()
                .stream()
                .map(Debitor -> {
                    return DebitorMapper.toDto(Debitor);
                }).collect(Collectors.toList());

        return DebitorDtoList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<DebitorDTO> update(@RequestBody DebitorDTO DebitorDto , @PathVariable @Min(1) Long id) {

        Optional<Debitor> DebitorOptional = DebitorService.getById(id);

        if (!DebitorOptional.isPresent()) {
            throw new EntityNotFoundException(Debitor.class, "id", id.toString());
        }

        Debitor Debitor = DebitorMapper.toEntity(DebitorDto );
        Debitor = DebitorService.update(Debitor, id);
        DebitorDto  = DebitorMapper.toDto(Debitor);

        return new ResponseEntity<>(DebitorDto , HttpStatus.OK);

    }

    @DeleteMapping(endpoint + "/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {

        // Debitor not found by the id
        Optional<Debitor> DebitorOptional = DebitorService.getById(id);

        if (!DebitorOptional.isPresent()) {
            throw new EntityNotFoundException(Debitor.class, "id", id.toString());
        }

        // do the delete
        DebitorService.delete(id);

        // response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }

}