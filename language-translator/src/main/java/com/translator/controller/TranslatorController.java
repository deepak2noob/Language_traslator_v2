package com.translator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;

@Controller
public class TranslatorController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/")
    public String home() {
        return "translator";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    
    @PostMapping("/translate")
    @ResponseBody
    public String translate(@RequestParam String text, @RequestParam String fromLang, @RequestParam String toLang) {
        String url = String.format("https://api.mymemory.translated.net/get?q=%s&langpair=%s|%s", 
            text, fromLang, toLang);
        return restTemplate.getForObject(url, String.class);
    }
}