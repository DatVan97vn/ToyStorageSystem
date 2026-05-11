package com.example.project4.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private String secret;
    private boolean using2FA;
    private boolean firstLogin;

    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public boolean isUsing2FA() { return using2FA; }
    public void setUsing2FA(boolean using2FA) { this.using2FA = using2FA; }
    public boolean isFirstLogin() { return firstLogin; }

    public void setFirstLogin(boolean firstLogin) {  this.firstLogin = firstLogin;  }
}