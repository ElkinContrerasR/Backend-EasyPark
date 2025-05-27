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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class UserServiceImpl implements IUserService{

    

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;
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
       // Verificar si la contraseña ya está encriptada (asumiendo que las encriptadas tienen 64 caracteres)
    if (user.getPassword() != null && user.getPassword().length() != 64) {
        user.setPassword(encryptPassword(user.getPassword()));
    }
       
        
        
        return userRepository.save(user);
    }
    @Override
    public void deleteUser(Integer id){
        
        userRepository.deleteById(id);;
    } 

    @Override
    public String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }

    
    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        
        if (user != null && user.getPassword().equals(encryptPassword(password))) {
            return user;
        }
        return null;
    }
    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("contrerasrojaselkinstiven@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        
       mailSender.send(message);
    }
    @Override
public User findByEmailAndDocument(String email, String document) {
    return userRepository.findByEmailAndDocument(email, document);
}

@Override
public String generateTemporaryPassword() {
    int length = 8;
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder password = new StringBuilder();
    
    for (int i = 0; i < length; i++) {
        int randomIndex = (int) (Math.random() * chars.length());
        password.append(chars.charAt(randomIndex));
    }
    return password.toString();
}


  
    

}
