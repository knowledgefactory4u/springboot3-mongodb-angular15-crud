package com.knf.dev.demo.crudapplication.controller;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knf.dev.demo.crudapplication.model.User;
import com.knf.dev.demo.crudapplication.exception.ResourceNotFoundException;
import com.knf.dev.demo.crudapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // create user rest API
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        SecureRandom random = new SecureRandom();
        user.setId(user.getEmailId()+"@"+random.nextInt(1000));
        return userRepository.save(user);
    }

    // get user by id rest api
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                      ("User not exist with id :" + id));
        return ResponseEntity.ok(user);
    }

    // update user rest api
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id,
             @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                      ("User not exist with id :" + id));
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmailId(userDetails.getEmailId());
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    // delete user rest api
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser
               (@PathVariable String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
            ("User not exist with id :" + id));
        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}