package com.translator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import jakarta.validation.Valid;
import com.translator.model.User;
import com.translator.model.TranslationRequest;
import com.translator.service.UserService;

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
        model.addAttribute("translationRequest", new TranslationRequest());
        return "translator";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, 
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

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
    public ResponseEntity<String> translate(@Valid @RequestBody TranslationRequest request,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                .body("{\"error\": \"Validation failed: " + 
                      bindingResult.getAllErrors().get(0).getDefaultMessage() + "\"}");
        }

        try {
            String encodedText = java.net.URLEncoder.encode(request.getText(), "UTF-8");
            String url = String.format(
                "https://api.mymemory.translated.net/get?q=%s&langpair=%s|%s", 
                encodedText, request.getFromLang(), request.getToLang()
            );
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                .status(500)
                .body("{\"responseStatus\":\"500\"," +
                      "\"responseData\":{\"translatedText\":\"Translation service error: " + 
                      e.getMessage() + "\"}}");
        }
    }
}