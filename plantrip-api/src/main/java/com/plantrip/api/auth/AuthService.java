package com.plantrip.api.auth;

import com.plantrip.api.user.User;
import com.plantrip.api.user.UserRepository;
import com.plantrip.api.user.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        String email = request.email().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new
                    ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        String passwordHash = passwordEncoder.encode(request.password());
        User user = new User(email, passwordHash);
        User saved = userRepository.save(user);
        return UserResponse.from(saved);

    }
}
