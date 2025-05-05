package com.br.apprelacionamento.models;

public class ProfileResponse {
    private String ethnicity;
    private String education;
    private String maritalStatus;
    private String desiredRelationship;
    private String bio;
    private String profession;
    private String profilePicture;
    private int age;
    private String interests;

    // Getters e Setters
    public String getEthnicity() { return ethnicity; }
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public String getDesiredRelationship() { return desiredRelationship; }
    public void setDesiredRelationship(String desiredRelationship) { this.desiredRelationship = desiredRelationship; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }
}

