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
    private String profilePictureBase64;

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
    public String getProfilePictureBase64() { return profilePictureBase64; }

    // Setters (opcional, dependendo se você for editar esse perfil depois)
    // Setters
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }
    public void setEducation(String education) { this.education = education; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }
    public void setDesiredRelationship(String desiredRelationship) { this.desiredRelationship = desiredRelationship; }
    public void setBio(String bio) { this.bio = bio; }
    public void setProfession(String profession) { this.profession = profession; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setInterests(List<String> interests) { this.interests = interests; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
    public void setProfilePictureBase64(String profilePictureBase64) { this.profilePictureBase64 = profilePictureBase64; }


}

