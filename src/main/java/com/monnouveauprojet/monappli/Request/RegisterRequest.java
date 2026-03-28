package com.monnouveauprojet.monappli.Request;

import com.monnouveauprojet.monappli.Model.Role;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    }
