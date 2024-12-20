package com.translator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;
import com.translator.model.User;
import com.translator.service.UserService;
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
    public String translate(@RequestParam String text, @RequestParam String fromLang, @RequestParam String toLang) {
        String url = String.format("https://api.mymemory.translated.net/get?q=%s&langpair=%s|%s", 
            text, fromLang, toLang);
        return restTemplate.getForObject(url, String.class);
    }
}
