package com.translator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;
import com.translator.model.User;
import com.translator.service.UserService;

@Controller
public class TranslatorController {
    
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
    public String registerUser(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/login";
    }
    
    @PostMapping("/translate")
    @ResponseBody
    public String translate(@RequestParam String text, @RequestParam String fromLang, @RequestParam String toLang) {
        String url = String.format("https://api.mymemory.translated.net/get?q=%s&langpair=%s|%s", 
            text, fromLang, toLang);
        return restTemplate.getForObject(url, String.class);
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            userService.save(user);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
}
}

    