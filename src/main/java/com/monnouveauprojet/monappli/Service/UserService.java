package com.monnouveauprojet.monappli.Service;

import com.monnouveauprojet.monappli.Model.Role;
import com.monnouveauprojet.monappli.Model.User;
import com.monnouveauprojet.monappli.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
public class UserService {
    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder
            passwordEncoder) {
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String name, String email, String password,
                             Role role) {
        User user = new User();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email déjà utilisé.");
        }
        user.setName(name);
        user.setEmail(email.toLowerCase());
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(false);
        user.setRole(role);

        // 🔹 Générer un code d'activation alphanumérique de 5 caractères
        String activationCode = UUID.randomUUID().toString().replace("-",
                "").substring(0, 5);
        user.setActivationCode(activationCode);

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByActivationCode(String code) {
        return userRepository.findByActivationCode(code);

    }

}