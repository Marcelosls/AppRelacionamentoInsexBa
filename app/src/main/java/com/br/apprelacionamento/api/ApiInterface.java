package com.br.apprelacionamento.api;

import com.br.apprelacionamento.models.LoginRequest;
import com.br.apprelacionamento.models.LoginResponse;
import com.br.apprelacionamento.models.ProfileRequest;
import com.br.apprelacionamento.models.UserRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/auth/register")
    Call<ResponseBody> registerUser(@Body UserRequest userRequest);
    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("insexba/register/profile")
    Call<Void> createProfile(
            @Header("Authorization") String token,
            @Body ProfileRequest profileRequest
    );

}

