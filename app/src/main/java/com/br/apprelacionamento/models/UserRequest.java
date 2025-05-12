package com.br.apprelacionamento.models;

public class UserRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String birthData; // formato "yyyy-MM-dd"
    private String gender;     // Ex: "Masculino", "Feminino", "Nao_Binario"
    private String user;   // Sempre "Usuario"

    public UserRequest(String firstName, String lastName, String password, String email,
                       String birthData, String gender, String user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.birthData = birthData;
        this.gender = gender;
        this.user = user;
    }

    // Getters e Setters (se forem necessários)
}
