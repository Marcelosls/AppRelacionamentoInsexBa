package com.br.apprelacionamento.models;

public class ProfileRequest {
    private String ethnicity;
    private String education;
    private String maritalStatus;
    private String desiredRelationship;
    private String bio;
    private String profession;

    public ProfileRequest(String ethnicity, String education, String maritalStatus, String desiredRelationship, String bio, String profession) {
        this.ethnicity = ethnicity;
        this.education = education;
        this.maritalStatus = maritalStatus;
        this.desiredRelationship = desiredRelationship;
        this.bio = bio;
        this.profession = profession;
    }
}


