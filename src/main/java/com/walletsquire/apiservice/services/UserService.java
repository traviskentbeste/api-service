package com.walletsquire.apiservice.services;

import com.walletsquire.apiservice.entities.User;
import com.walletsquire.apiservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(User user) {

        return userRepository.save(user);

    }

    public Optional<User> getById(Long id) {

        return userRepository.findById(id);

    }

    public List<User> getAll() {

        return userRepository.findAll();

    }

    public User update(User user, Long id) {

        Optional<User> userOptional = userRepository.findById(id);

        if (! userOptional.isPresent()) {
            return null;
        }

        User existingUser = userOptional.get();

        if ( (user.getFirstName() != null) && (! user.getFirstName().isEmpty()) ) {
            existingUser.setFirstName(user.getFirstName());
        }
        if ( (user.getLastName() != null) && (! user.getLastName().isEmpty()) ) {
            existingUser.setLastName(user.getLastName());
        }
    
        return userRepository.save(existingUser);

    }

    public void delete(User user) {

        Optional<User> userOptional = userRepository.findById(user.getId());

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        }

    }

    public void delete(Long id) {

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        }

    }

}
