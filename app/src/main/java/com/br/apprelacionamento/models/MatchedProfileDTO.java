package com.br.apprelacionamento.models;

import java.util.List;

public class MatchedProfileDTO {

    private Integer userId;
    private String firstName;
    private String lastName;
    private int age;
    private String bio;
    private String profession;
    private List<String> commonInterests;
    private double matchPercentage;
    private String profilePictureBase64;

    public Integer getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getBio() { return bio; }
    public String getProfession() { return profession; }
    public List<String> getCommonInterests() { return commonInterests; }
    public double getMatchPercentage() { return matchPercentage; }
    public String getProfilePictureBase64() { return profilePictureBase64; }
}
