package com.br.apprelacionamento.api;

import com.br.apprelacionamento.models.LoginRequest;
import com.br.apprelacionamento.models.LoginResponse;
import com.br.apprelacionamento.models.UserRequest;
import com.br.apprelacionamento.models.ProfileResponseDTO;
import com.br.apprelacionamento.models.ProfileRequestDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/auth/register")
    Call<ResponseBody> registerUser(@Body UserRequest userRequest);

    @POST("/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("/perfil/{userId}")
    Call<ProfileResponseDTO> getProfile(@Path("userId") int userId);

    @POST("/perfil")
    Call<ResponseBody> updateProfile(@Body ProfileRequestDTO profileRequestDTO);

    @POST("/login")
    @FormUrlEncoded
    Call<LoginResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("/profile/{userId}")
    Call<ResponseBody> createProfile(@Path("userId") int userId);

}
