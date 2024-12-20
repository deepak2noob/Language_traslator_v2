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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

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
            userService.save(user);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    
    @PostMapping("/translate")
    @ResponseBody
    public ResponseEntity<String> translate(
            @RequestParam String text,
            @RequestParam String fromLang,
            @RequestParam String toLang) {
        try {
            String url = String.format(
                "https://api.mymemory.translated.net/get?q=%s&langpair=%s|%s", 
                text, fromLang, toLang
            );
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(response);
        } catch (RestClientException e) {
            logger.error("Translation error: {}", e.getMessage());
            return ResponseEntity
                .status(500)
                .body("{\"responseStatus\":\"500\"," +
                      "\"responseData\":{\"translatedText\":\"Translation service error\"}}");
        }
    }
}