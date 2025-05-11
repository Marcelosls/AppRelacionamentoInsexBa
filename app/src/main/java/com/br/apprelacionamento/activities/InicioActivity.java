package com.br.apprelacionamento.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InicioActivity extends AppCompatActivity {

    private static final String TAG = "FCM";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Obtém o token
                    String token = task.getResult();
                    Log.d(TAG, "Token manual: " + token);

                    // Envia o token para o servidor
                    enviarTokenParaServidor(token);
                });
    }

    private void enviarTokenParaServidor(String token) {
        // Corpo da requisição
        NotificacaoRequest request = new NotificacaoRequest(
                "Você tem um novo match!",
                "Alguém gostou de você ❤️",
                null,
                token,
                new HashMap<>() // Pode adicionar dados extras aqui se quiser
        );

        ApiService api = RetrofitClient.getApiService();
        api.enviarNotificacao(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "Notificação enviada com sucesso");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Erro ao enviar notificação", t);
            }
        });
    }

    // RetrofitClient interno
    public static class RetrofitClient {
        private static final String BASE_URL = "http://10.0.2.2:8080";
        private static Retrofit retrofit;

        public static ApiService getApiService() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit.create(ApiService.class);
        }
    }

    // Interface da API
    public interface ApiService {
        @retrofit2.http.Headers("Content-Type: application/json")
        @retrofit2.http.POST("/notification/notification/token")
        Call<Void> enviarNotificacao(@retrofit2.http.Body NotificacaoRequest request);
    }

    // Modelo da requisição
    public static class NotificacaoRequest {
        private String title;
        private String body;
        private String image;
        private String recipientToken;
        private Map<String, String> data;

        public NotificacaoRequest(String title, String body, String image, String recipientToken, Map<String, String> data) {
            this.title = title;
            this.body = body;
            this.image = image;
            this.recipientToken = recipientToken;
            this.data = data;
        }
    }
}
