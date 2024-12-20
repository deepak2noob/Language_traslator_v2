package com.translator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;
import com.translator.model.User;
import com.translator.service.UserService;

import java.nio.charset.StandardCharsets;

import org.apache.catalina.util.RateLimiter;
import org.hibernate.annotations.processing.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TranslatorController {
    
    private static final Logger logger = LoggerFactory.getLogger(TranslatorController.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String home() {
        return "translator";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            logger.info("Attempting to register user: {}", user.getUsername());
            
            // Validate input
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                model.addAttribute("error", "Username cannot be empty");
                return "register";
            }
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                model.addAttribute("error", "Email cannot be empty");
                return "register";
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                model.addAttribute("error", "Password cannot be empty");
                return "register";
            }
            
            userService.save(user);
            logger.info("Successfully registered user: {}", user.getUsername());
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage(), e);
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
    
    @PostMapping("/translate")
    @ResponseBody
    public ResponseEntity<?> translate(
        @RequestParam @Size(min = 1, max = 5000) String text, 
        @RequestParam @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$") String fromLang,
        @RequestParam @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$") String toLang) {
        
        try {
            // Encode parameters to prevent injection
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
            String url = String.format("https://api.mymemory.translated.net/get?q=%s&langpair=%s|%s", 
                encodedText, fromLang, toLang);
                
            // Add rate limiting
            RateLimiter.checkLimit(SecurityContextHolder.getContext().getAuthentication().getName());
                
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Translation error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Translation service error: " + e.getMessage());
        }
}
}