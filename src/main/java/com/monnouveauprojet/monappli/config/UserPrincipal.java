package com.monnouveauprojet.monappli.config;

import com.monnouveauprojet.monappli.Model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    // Constructeur
    public UserPrincipal(Long id, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Cette méthode transforme ton entité 'User' de la base de données
     * en un objet 'UserPrincipal' compréhensible par Spring Security.
     */
    public static UserPrincipal create(User user) {
        // 1. On récupère le nom du rôle (ex: ADMIN ou USER)
        String roleName = (user.getRole() != null) ? user.getRole().name() : "USER";

        // 2. On s'assure qu'il commence par "ROLE_" pour Spring Security
        String finalRole = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;

        // 3. On crée la liste des permissions (authorities)
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(finalRole)
        );

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // --- On force tout à true pour éviter les blocages de compte en test ---
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}