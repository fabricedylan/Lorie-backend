package com.monnouveauprojet.monappli.Service;

import com.monnouveauprojet.monappli.Model.User;
import com.monnouveauprojet.monappli.Repository.UserRepository;
import com.monnouveauprojet.monappli.config.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Charge l'utilisateur à partir de son email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Utilisateur introuvable avec l'email : " + email));
        return UserPrincipal.create(user);
    }

// Méthode auxiliaire pour charger l'utilisateur à partir de L'ID
public UserDetails loadUserById(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() ->
                    new UsernameNotFoundException("Utilisateur introuvable avec l'id : " + id));
    return UserPrincipal.create(user);
}
}
