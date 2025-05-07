package com.br.apprelacionamento.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;
import com.br.apprelacionamento.models.Contact; // Supondo que você tenha essa classe model

public class ContactsClient {
    private static Retrofit retrofit;

    // URL base da sua API
    private static final String BASE_URL = "http://10.0.2.2:8080/"; // IP para acessar o localhost do host no emulador

    // Interface que define os endpoints da API
    public interface ApiService {
        @GET("matches") // Alterar para o endpoint correto da sua API
        Call<List<Contact>> getMatchedContacts();
    }

    // Método para obter a instância Retrofit
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Converte JSON para objetos Java
                    .build();
        }
        return retrofit;
    }

    // Método para obter a instância da ApiService
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}

