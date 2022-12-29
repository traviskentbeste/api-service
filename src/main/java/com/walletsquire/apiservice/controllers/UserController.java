package com.walletsquire.apiservice.controllers;

import com.walletsquire.apiservice.dtos.UserDTO;
import com.walletsquire.apiservice.entities.User;
import com.walletsquire.apiservice.exceptions.EntityNotFoundException;
import com.walletsquire.apiservice.mappers.UserMapper;
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
public class UserController {
    public static final String endpoint = "/user";

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = endpoint)
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO userDto) {

        User user = userMapper.toEntity(userDto);
        user = userService.create(user);
        userDto = userMapper.toDto(user);

        return new ResponseEntity<>(userDto , HttpStatus.CREATED);

    }

    @GetMapping(value = endpoint + "/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") @Min(1) Long id) {

        Optional<User> userOptional = userService.getById(id);

        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException(User.class, "id", id.toString());
        }

        UserDTO userDto  = userMapper.toDto(userOptional.get());

        return new ResponseEntity<>(userDto , HttpStatus.OK);

    }

    @GetMapping( value = endpoint)
    public List<UserDTO> getAll() {

        List<UserDTO> userDtoList = userService.getAll()
                .stream()
                .map(user -> {
                    return userMapper.toDto(user);
                }).collect(Collectors.toList());

        return userDtoList;

    }

    @PutMapping(endpoint + "/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDto , @PathVariable @Min(1) Long id) {

        Optional<User> userOptional = userService.getById(id);

        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException(User.class, "id", id.toString());
        }

        User user = userMapper.toEntity(userDto );
        user = userService.update(user, id);
        userDto  = userMapper.toDto(user);

        return new ResponseEntity<>(userDto , HttpStatus.OK);

    }

    @DeleteMapping(endpoint + "/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Long id) {

        // user not found by the id
        Optional<User> userOptional = userService.getById(id);

        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException(User.class, "id", id.toString());
        }

        // do the delete
        userService.delete(id);

        // response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }

}