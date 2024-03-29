package com.controllers;

import com.models.Users;
import com.repositories.UserJPARepisitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
