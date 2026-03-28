package com.monnouveauprojet.monappli.Controller;

import com.monnouveauprojet.monappli.Model.User;
import com.monnouveauprojet.monappli.Repository.UserRepository;
import com.monnouveauprojet.monappli.Request.LoginRequest;
import com.monnouveauprojet.monappli.Request.RegisterRequest;
import com.monnouveauprojet.monappli.Response.JwtAuthenticationResponse;
import com.monnouveauprojet.monappli.Service.EmailService;
import com.monnouveauprojet.monappli.Service.UserService;
import com.monnouveauprojet.monappli.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthController(UserRepository userRepository, EmailService emailService, UserService userService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    // ------------------ REGISTER ------------------
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {

        User user = userService.registerUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );
        String activationLink = "http://localhost:4200/activate?code=" + user.getActivationCode();
        String htmlContent = "<h3>Bienvenue sur notre plateforme !</h3>" +
                "<p>Merci pour votre inscription.</p>" +
                "<p>Votre code d'activation : <strong>" + user.getActivationCode() + "</strong></p>" +
                "<p><a href=\"" + activationLink + "\" " +
                "style=\"display:inline-block;padding:10px 20px;background-color:#007bff;color:white;" +
                "text-decoration:none;border-radius:5px;\">Activer mon compte</a></p>" +
                "<p>Si le bouton ne fonctionne pas, copiez-collez ce lien dans votre navigateur :<br/>" +
                "<a href=\"" + activationLink + "\">" + activationLink + "</a></p>";


        // ✅ Nouveau lien pointant vers le frontend Angular


        // ✅ Email avec lien d'activation frontend
        emailService.sendEmail(
                request.getEmail(),
                "Activation de votre compte",
                htmlContent
        );

        return ResponseEntity.ok(Collections.singletonMap(
                "message", "Inscription réussie. Vérifiez votre email pour activer votre compte."
        ));

    }

    // ------------------ ACTIVATE ------------------
    @PostMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam String
                                                          code) {
        Optional<User> userOptional =
                userService.findByActivationCode(code);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
// Vérifie si le code d'activation est correct
            if (user.getActivationCode().equals(code)) {
                user.setEnabled(true);
                user.setActivationCode(null); // Supprime le code après

                userRepository.save(user);
                return ResponseEntity.ok("Compte activé avec succès !");
            } else {
                return ResponseEntity.badRequest().body("Code d'activation invalide !");
            }
        } else {
            return ResponseEntity.badRequest().body("Utilisateur introuvable !");
        }
    }
    // ------------------ LOGIN ------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> userOptional = userService.findByEmail(loginRequest.getEmail());
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Utilisateur introuvable."));
            }
            User user = userOptional.get();
            if (!user.isEnabled()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Votre compte n'est pas activé."));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Collections.singletonMap("message", "Erreur interne du serveur : " + e.getMessage()));
        }
    }

    // ------------------ CLASSE ACTIVATION ------------------
    public static class ActivationRequest {
        private String code;
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }
}




