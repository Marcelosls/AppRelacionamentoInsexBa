package com.br.apprelacionamento.api;

import com.br.apprelacionamento.models.UserRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/insexba/register")
    Call<ResponseBody> registerUser(@Body UserRequest userRequest);
}
