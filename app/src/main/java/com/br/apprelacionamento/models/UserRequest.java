package com.br.apprelacionamento.models;

public class UserRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String birth_Data; // formato "yyyy-MM-dd"
    private String gender;     // Ex: "Masculino", "Feminino", "Nao_Binario"
    private String typeUser;   // Sempre "Usuario"
    private int agr;           // Sempre 1

    public UserRequest(String firstName, String lastName, String password, String email,
                       String birth_Data, String gender, String typeUser, int agr) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.birth_Data = birth_Data;
        this.gender = gender;
        this.typeUser = typeUser;
        this.agr = agr;
    }

    // Getters e Setters (se forem necessários)
}
