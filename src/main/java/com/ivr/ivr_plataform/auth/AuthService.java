package com.ivr.ivr_plataform.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository repo,
            PasswordEncoder encoder,
            JwtService jwtService) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public void register(AuthRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                encoder.encode(request.getPassword()));

        if (repo.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username  Already Exist");
        }
        repo.save(user);
    }

    public String login(AuthRequest request) {

        User user = repo.findByUsername(
                request.getUsername()).orElseThrow();

        boolean valid = encoder.matches(
                request.getPassword(),
                user.getPassword());

        if (!valid) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.generateToken(user.getUsername());
    }
}
