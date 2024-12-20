package com.translator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.translator.model.User;
import com.translator.repository.UserRepository;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by username: {}", username);
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                logger.error("User not found: {}", username);
                return new UsernameNotFoundException("User not found: " + username);
            });

        logger.debug("User found: {}", username);
        
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
}