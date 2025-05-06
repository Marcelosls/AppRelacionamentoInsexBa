package com.br.apprelacionamento.api;

import com.br.apprelacionamento.models.InterestsRequest;
import com.br.apprelacionamento.models.LoginRequest;
import com.br.apprelacionamento.models.LoginResponse;
import com.br.apprelacionamento.models.ProfileRequest;
import com.br.apprelacionamento.models.ProfileResponse;
import com.br.apprelacionamento.models.UserRequest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/auth/register")
    Call<ResponseBody> registerUser(@Body UserRequest userRequest);
    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @Multipart
    @POST("insexba/register/profile")
    Call<ResponseBody> createProfile(
            @Header("Authorization") String token,
            @Part("ethnicity") RequestBody ethnicity,
            @Part("education") RequestBody education,
            @Part("maritalStatus") RequestBody maritalStatus,
            @Part("desiredRelationship") RequestBody desiredRelationship,
            @Part("bio") RequestBody bio,
            @Part("profession") RequestBody profession,
            @Part MultipartBody.Part profilePicture,
            @Part("age") RequestBody age,
            @Part("interests") RequestBody interests
    );

    @GET("insexba/user/{userId}")
    Call<ProfileResponse> getProfile(@Header("Authorization") String token, @Path("userId") int userId);

    @POST("/insexba/interests")
    Call<Void> createInterests(@Body InterestsRequest interestsRequest);



}

