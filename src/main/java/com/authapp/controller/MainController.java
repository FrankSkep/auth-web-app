package com.authapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        } else {
            model.addAttribute("username", "No authenticated");
        }
        return "index";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public void admin(Model model) {
        model.addAttribute("message", "Autorizado, admin");
    }

    @GetMapping("/user")
    public void user(Model model) {
        model.addAttribute("message", "Autorizado, user");
    }
}

