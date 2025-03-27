package com.corhuila.easypark.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.corhuila.easypark.models.User;
import com.corhuila.easypark.services.IUserService;

@RestController
@RequestMapping("/api/easypark")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    IUserService userService;

    @GetMapping("/getAll")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    @PostMapping
    public User  createUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    // Manejo de error para email duplicado
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateEmailError(DataIntegrityViolationException ex) {
        return "El email ya está registrado.";
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
    }




}
