package com.safenews.api.service;

import com.safenews.api.dto.AuthRequestDTO;
import com.safenews.api.exception.SetupAlreadyCompletedException;
import com.safenews.api.model.User;
import com.safenews.api.model.UserRole;
import com.safenews.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with email " + username + " not found");
        }

        return user;
    }

    public boolean isSystemInitialized() {
        return userRepository.count() > 0;
    }

    public void register(AuthRequestDTO dto, PasswordEncoder passwordEncoder) {
        if (isSystemInitialized()) {
            throw new SetupAlreadyCompletedException(("The system already have a user"));
        }

        User newUSer = new User();
        newUSer.setEmail(dto.email());

        String encryptedPassword = passwordEncoder.encode(dto.password());
        newUSer.setPassword(encryptedPassword);

        newUSer.setRole(UserRole.ADMIN);

        userRepository.save(newUSer);
    }
}
