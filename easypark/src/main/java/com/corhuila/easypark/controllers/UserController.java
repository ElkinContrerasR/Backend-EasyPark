package com.corhuila.easypark.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


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
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        User authenticatedUser = userService.login(loginUser.getEmail(), loginUser.getPassword());

        if (authenticatedUser != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inicio de sesión exitoso");
            response.put("user", Map.of(
                "id", authenticatedUser.getId(),
                "email", authenticatedUser.getEmail(),
                "nombres", authenticatedUser.getNombres(),
                "rol", authenticatedUser.getRol()
            ));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401)
                .body(Collections.singletonMap("error", "Credenciales incorrectas"));
        }
    }

    @GetMapping("/sendTestEmail")
    public String sendTestEmail() {
        userService.sendEmail("escontreras-2023a@corhuila.edu.co", "Prueba de correo", "¡Hola! Este es un correo de prueba desde Spring Boot.");
        return "Correo enviado con éxito";
    }

    @PostMapping("/recover-password")
public ResponseEntity<?> recoverPassword(@RequestBody User recoveryUser) {
    User user = userService.findByEmailAndDocument(recoveryUser.getEmail(), recoveryUser.getDocument());
    
    if (user != null) {
        String newPassword = userService.generateTemporaryPassword();
        user.setPassword(userService.encryptPassword(newPassword));
        userService.saveUser(user); // Guarda la nueva contraseña encriptada
        
        
        
        // Enviar correo con la nueva contraseña temporal
        String subject = "Recuperación de contraseña - EasyPark";
        String body = "Hola, " + user.getEmail() + "\n\n" +
                      "Tu nueva contraseña temporal es: " + newPassword + "\n\n" +
                      "Por favor, inicia sesión y cambia tu contraseña lo antes posible.";

        userService.sendEmail(user.getEmail(), subject, body);

        return ResponseEntity.ok(Collections.singletonMap("message", "Se ha enviado una nueva contraseña a tu correo."));
    } else {
        return ResponseEntity.status(404).body(Collections.singletonMap("error", "No se encontró un usuario con esos datos."));
    }
}

    
}





