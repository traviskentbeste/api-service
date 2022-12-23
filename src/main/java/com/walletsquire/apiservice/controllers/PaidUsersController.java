package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.PaidDTO;
import com.walletsquire.apiservice.dtos.PaidUsersDTO;
import com.walletsquire.apiservice.dtos.UserDTO;
import com.walletsquire.apiservice.entities.Paid;
import com.walletsquire.apiservice.entities.PaidUsers;
import com.walletsquire.apiservice.entities.User;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.PaidMapper;
import com.walletsquire.apiservice.mappers.PaidUsersMapper;
import com.walletsquire.apiservice.mappers.UserMapper;
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
    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = endpoint)
    public ResponseEntity<PaidUsersDTO> create(@Valid @RequestBody PaidUsersDTO paidUsersDto) {

        // need to make sure the paid and user exist in order to create this
        if (paidUsersDto.getPaid() != null) {

            Optional<Paid> paidOptional = paidService.getById(paidUsersDto.getPaid().getId());

            if (paidOptional.isPresent()) {

                if (paidUsersDto.getUser() != null) {

                    Optional<User> userOptional = userService.getById(paidUsersDto.getUser().getId());

                    if (userOptional.isPresent()) {

                        PaidUsers paidUsers = paidUsersMapper.toEntity(paidUsersDto);
                        paidUsers = paidUsersService.create(paidUsers);
                        paidUsersDto = paidUsersMapper.toDto(paidUsers);

                        Paid paid = paidOptional.get();
                        // need to update paidUsersDto.paid
                        PaidDTO paidDTO = paidUsersDto.getPaid();
                        paidDTO.setId(paid.getId());
                        paidDTO.setType(paid.getType());
                        paidDTO.setHasExtraPenny(paid.getHasExtraPenny());
                        paidDTO.setCalculatedSplitAmount(paid.getCalculatedSplitAmount());
                        paidUsersDto.setPaid(paidDTO);

                        return new ResponseEntity<>(paidUsersDto , HttpStatus.CREATED);

                    } else {
                        throw new EntityNotFoundException(User.class, "id", paidUsersDto.getUser().getId().toString());
                    }
                } else {
                    //System.out.println("issue where user is null");
                    throw new EntityNotFoundException(User.class, "id", "cannot be null");
                }
            }
            throw new EntityNotFoundException(Paid.class, "id", paidUsersDto.getPaid().getId().toString());
        }
        //System.out.println("issue where paid is null");
        throw new EntityNotFoundException(Paid.class, "id", "cannot be null");

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<PaidUsersDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<PaidUsers> paidUsersOptional = paidUsersService.getById(id);

        if (!paidUsersOptional.isPresent()) {
            throw new EntityNotFoundException(PaidUsers.class, "id", id.toString());
        }

        PaidUsersDTO paidUsersDto  = paidUsersMapper.toDto(paidUsersOptional.get());

        return new ResponseEntity<>(paidUsersDto , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<PaidUsersDTO> getAll() {

        List<PaidUsersDTO> paidUsersDtoList = paidUsersService.getAll()
                .stream()
                .map(paidUsers -> {
                    return paidUsersMapper.toDto(paidUsers);
                }).collect(Collectors.toList());

        return paidUsersDtoList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<PaidUsersDTO> update(@RequestBody PaidUsersDTO paidUsersDto , @PathVariable @Min(1) Long id) {

        Optional<PaidUsers> paidUsersOptional = paidUsersService.getById(id);

        if (!paidUsersOptional.isPresent()) {
            throw new EntityNotFoundException(PaidUsers.class, "id", id.toString());
        }

        PaidUsers paidUsers = paidUsersService.update(paidUsersOptional.get(), paidUsersDto, id);

        PaidDTO paidDTO = new PaidDTO();
        paidDTO.setId(paidUsers.getPaid().getId());
        paidDTO.setType(paidUsers.getPaid().getType());
        paidDTO.setHasExtraPenny(paidUsers.getPaid().getHasExtraPenny());
        paidDTO.setCalculatedSplitAmount(paidUsers.getPaid().getCalculatedSplitAmount());

        UserDTO userDTO = userMapper.toDto(paidUsers.getUser());
        paidUsersDto.setAmount(paidUsers.getAmount());
        paidUsersDto.setPaid(paidDTO);
        paidUsersDto.setUser(userDTO);

        // set the id
        paidUsersDto.setId(paidUsersOptional.get().getId());

        // set the amount
        paidUsersDto.setAmount(paidUsersOptional.get().getAmount());

        return new ResponseEntity<>(paidUsersDto , HttpStatus.OK);

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