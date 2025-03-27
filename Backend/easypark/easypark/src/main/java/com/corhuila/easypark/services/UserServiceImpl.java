package com.corhuila.easypark.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corhuila.easypark.models.User;
import com.corhuila.easypark.repositories.IUserRepository;

@Service
public class UserServiceImpl implements IUserService{

    

    @Autowired
    private IUserRepository userRepository;
    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @Override
    public Optional<User> getUserById(Integer id){
        return userRepository.findById(id);
    }
    @Override
    public User saveUser(User user){
       
       
        user.setPassword(encryptPassword(user.getPassword()));
       
        
        return userRepository.save(user);
    }
    @Override
    public void deleteUser(Integer id){
        
        userRepository.deleteById(id);;
    } 


    public String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }
    

}
