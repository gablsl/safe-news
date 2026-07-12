package com.safenews.api.controller;

import com.safenews.api.dto.AuthRequestDTO;
import com.safenews.api.dto.AuthResponseDTO;
import com.safenews.api.dto.SetupStatusDTO;
import com.safenews.api.model.User;
import com.safenews.api.service.AuthenticationService;
import com.safenews.api.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@NullMarked
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/status")
    public ResponseEntity<SetupStatusDTO> getStatus() {
        boolean isInitialized = authenticationService.isSystemInitialized();
        return ResponseEntity.ok(new SetupStatusDTO(isInitialized));
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(@RequestBody @Valid AuthRequestDTO dto) {
        authenticationService.register(dto, passwordEncoder);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        // This triggers the loadUserByUsername validation and password hashing checks
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String jwtToken = tokenService.generateToken((User) Objects.requireNonNull(authentication.getPrincipal()));

        return ResponseEntity.ok(new AuthResponseDTO(jwtToken));
    }
}
