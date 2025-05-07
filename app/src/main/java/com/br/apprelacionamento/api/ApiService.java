package com.br.apprelacionamento.api;

import com.br.apprelacionamento.models.ChatMessage;
import com.br.apprelacionamento.models.LoginRequest;
import com.br.apprelacionamento.models.LoginResponse;
import com.br.apprelacionamento.models.UserRequest;
import com.br.apprelacionamento.models.ProfileResponseDTO;
import com.br.apprelacionamento.models.ProfileRequestDTO;
import com.br.apprelacionamento.models.Contact;  // Importe a classe Contact, se não estiver importada

import java.util.List;

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

    // Endpoint para enviar uma nova mensagem de chat
    @POST("chat/messages")
    Call<ChatMessage> sendMessage(@Body ChatMessage chatMessage);

    // Endpoint para buscar todas as mensagens entre dois usuários (usando os IDs de remetente e destinatário)
    @GET("chat/messages/{senderId}/{receiverId}")
    Call<List<ChatMessage>> getMessages(@Path("senderId") int senderId, @Path("receiverId") int receiverId);

    // Endpoint para buscar os contatos que deram match
    @GET("matches")
    Call<List<Contact>> getMatchedContacts();
}
