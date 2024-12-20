package com.translator.service;

import com.translator.model.User;
import com.translator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public User registerUser(User user) {
        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    
    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}

