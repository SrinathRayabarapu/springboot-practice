package com.boot.controllers;

import com.boot.models.Users;
import com.boot.repositories.UserJPARepisitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    //usually we call a @service and it in turns calls a repository
    @Autowired
    private UserJPARepisitory userJPARepository;

    @GetMapping(value = "/all")
    public List<Users> findAll() {
        return userJPARepository.findAll();
    }

    @GetMapping(value = "/{name}")
    public Users findByName(@PathVariable final String name) {
        return userJPARepository.findByName(name);
    }

    @PostMapping(value = "/load")
    public Users load(@RequestBody final Users users) {
        return userJPARepository.save(users);
    }
}
