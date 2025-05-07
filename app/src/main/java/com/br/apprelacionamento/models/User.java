package com.br.apprelacionamento.models;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    // Construtor
    public User(){

    }

    // Getters
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    // Setters (opcional)
    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}