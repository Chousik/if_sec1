package org.chousik.infosec_1.controller;

import jakarta.validation.Valid;
import org.chousik.infosec_1.dto.AuthResponse;
import org.chousik.infosec_1.dto.CredentialsRequest;
import org.chousik.infosec_1.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody CredentialsRequest request) {
        return authService.login(request);
    }
}
