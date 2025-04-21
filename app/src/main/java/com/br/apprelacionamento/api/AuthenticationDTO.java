package com.br.apprelacionamento.api;

public class AuthenticationDTO {
    private String email;
    private String password;

    public AuthenticationDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters e setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

