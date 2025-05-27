package com.corhuila.easypark.services;

import java.util.List;
import java.util.Optional;

import com.corhuila.easypark.models.User;

public interface IUserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Integer id);
    User saveUser(User user);
    //User updateUser(Integer id, User userDetails);
    void deleteUser(Integer id);
    String encryptPassword(String password);
    User login(String email, String password); 
    void sendEmail(String to, String subject, String body);
    User findByEmailAndDocument(String email, String document);
    
    String generateTemporaryPassword();
    
   
}
