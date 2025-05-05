package com.br.apprelacionamento.models;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileRequest {
    private RequestBody ethnicity;
    private RequestBody education;
    private RequestBody maritalStatus;
    private RequestBody desiredRelationship;
    private RequestBody bio;
    private RequestBody profession;
    private RequestBody age;
    private RequestBody interests;
    private MultipartBody.Part profilePicture;

    // Getters e Setters
    public RequestBody getEthnicity() { return ethnicity; }
    public void setEthnicity(RequestBody ethnicity) { this.ethnicity = ethnicity; }

    public RequestBody getEducation() { return education; }
    public void setEducation(RequestBody education) { this.education = education; }

    public RequestBody getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(RequestBody maritalStatus) { this.maritalStatus = maritalStatus; }

    public RequestBody getDesiredRelationship() { return desiredRelationship; }
    public void setDesiredRelationship(RequestBody desiredRelationship) { this.desiredRelationship = desiredRelationship; }

    public RequestBody getBio() { return bio; }
    public void setBio(RequestBody bio) { this.bio = bio; }

    public RequestBody getProfession() { return profession; }
    public void setProfession(RequestBody profession) { this.profession = profession; }

    public RequestBody getAge() { return age; }
    public void setAge(RequestBody age) { this.age = age; }

    public RequestBody getInterests() { return interests; }
    public void setInterests(RequestBody interests) { this.interests = interests; }

    public MultipartBody.Part getProfilePicture() { return profilePicture; }
    public void setProfilePicture(MultipartBody.Part profilePicture) { this.profilePicture = profilePicture; }
}
