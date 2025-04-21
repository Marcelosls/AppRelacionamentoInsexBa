package com.br.apprelacionamento.models;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters e setters se o Retrofit exigir
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
