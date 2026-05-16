package com.ivr.ivr_plataform.auth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/me")
    public String me(Authentication authentication) {

        return "Hello " + authentication.getName();
    }
}
