package com.smartbook.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/users/me")
    public String getMyProfile(Authentication authentication) {

        return "Logged in as: " + authentication.getName();
    }
}