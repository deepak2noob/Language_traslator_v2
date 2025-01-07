package com.translator.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.translator.model.User;
import com.translator.repository.UserRepository;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUserSuccessfully() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        userService.save(user);

        verify(userRepository, times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void testSaveUserWithExistingUsername() {
        User user = new User();
        user.setUsername("existinguser");
        user.setPassword("password123");
        user.setEmail("existinguser@example.com");

        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.save(user);
        });

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void testSaveUserWithInvalidEmail() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("invalid-email");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.save(user);
        });

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void testSaveUserWithShortPassword() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("123");
        user.setEmail("testuser@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.save(user);
        });

        assertEquals("Password must be at least 6 characters long", exception.getMessage());
    }

    @Test
    void testSaveUserWithEmptyUsername() {
        User user = new User();
        user.setUsername("");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.save(user);
        });

        assertEquals("Username cannot be empty", exception.getMessage());
    }
}