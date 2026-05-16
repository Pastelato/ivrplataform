package com.ivr.ivr_plataform.auth;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String register(
            @RequestBody AuthRequest request) {

        service.register(request);

        return "User created";
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody AuthRequest request) {

        String token = service.login(request);

        return new AuthResponse(token);
    }
}
