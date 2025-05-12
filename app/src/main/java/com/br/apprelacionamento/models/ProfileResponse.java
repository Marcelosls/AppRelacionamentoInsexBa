package com.br.apprelacionamento.models;

import java.util.List;

public class ProfileResponse {
    private String ethnicity;
    private String education;
    private String maritalStatus;
    private String desiredRelationship;
    private String bio;
    private String profession;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private List<String> interests;
    private String profilePicture; // pode ser null ou base64 futuramente

    // Getters
    public String getEthnicity() { return ethnicity; }
    public String getEducation() { return education; }
    public String getMaritalStatus() { return maritalStatus; }
    public String getDesiredRelationship() { return desiredRelationship; }
    public String getBio() { return bio; }
    public String getProfession() { return profession; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public List<String> getInterests() { return interests; }
    public String getProfilePicture() { return profilePicture; }

    // Setters (opcional, dependendo se você for editar esse perfil depois)


}

