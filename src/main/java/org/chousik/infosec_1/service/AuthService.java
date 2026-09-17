package org.chousik.infosec_1.service;

import org.chousik.infosec_1.dto.AuthResponse;
import org.chousik.infosec_1.dto.CredentialsRequest;
import org.chousik.infosec_1.entity.UserAccount;
import org.chousik.infosec_1.repository.UserAccountRepository;
import org.chousik.infosec_1.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserAccountRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(CredentialsRequest request) {
        UserAccount user = userRepository.findById(request.login())
                .orElseThrow(this::invalidCredentials);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw invalidCredentials();
        }

        return new AuthResponse(jwtService.generateToken(user.getLogin()));
    }

    private ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid login or password"
        );
    }
}
