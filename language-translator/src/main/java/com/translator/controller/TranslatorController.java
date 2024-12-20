package com.translator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;
import com.translator.model.User;
import com.translator.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.RestClientException;

@Controller
public class TranslatorController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String redirectToTranslator() {
        return "redirect:/translator";
    }
    
    @GetMapping("/translator")
    public String translator(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }
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
            return ResponseEntity
                .status(500)
                .body("{\"responseStatus\":\"500\"," +
                      "\"responseData\":{\"translatedText\":\"Translation service error\"}}");
        }
    }
}