package com.br.apprelacionamento.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.br.apprelacionamento.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InicioActivity extends AppCompatActivity {

    private Button btnLogin, btnCadastro;
    private static final String TAG = "FCM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = preferences.getString("authToken", null);

        if (token != null && !token.isEmpty()) {
            Intent intent = new Intent(InicioActivity.this, MainNavigationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_inicio);

            TextView textView = findViewById(R.id.txtNomeApp);
            textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Shader textShader = new LinearGradient(
                            0, 0, textView.getWidth(), 0,
                            new int[]{
                                    Color.parseColor("#D2B162"),
                                    Color.parseColor("#A67C00"),
                                    Color.parseColor("#D2B162")
                            },
                            new float[]{0f, 0.5f, 1f},
                            Shader.TileMode.CLAMP
                    );
                    textView.getPaint().setShader(textShader);
                    textView.invalidate();
                }
            });

            btnLogin = findViewById(R.id.btnLogin);
            btnCadastro = findViewById(R.id.btnCadastro);

            btnLogin.setOnClickListener(view -> startActivity(new Intent(InicioActivity.this, LoginActivity.class)));
            btnCadastro.setOnClickListener(view -> startActivity(new Intent(InicioActivity.this, RegisterActivity.class)));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }

            // Gera e envia token FCM
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String fcmToken = task.getResult();
                    Log.d(TAG, "Token gerado: " + fcmToken);
                    enviarTokenParaServidor(fcmToken);
                } else {
                    Log.w(TAG, "Erro ao gerar token", task.getException());
                }
            });
        }
    }

    private void enviarTokenParaServidor(String token) {
        NotificacaoRequest request = new NotificacaoRequest(
                "Você tem um novo match!",
                "Alguém gostou de você ❤️",
                null,
                token,
                new HashMap<>()
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

    public interface ApiService {
        @retrofit2.http.Headers("Content-Type: application/json")
        @retrofit2.http.POST("/notification/notification/token")
        Call<Void> enviarNotificacao(@retrofit2.http.Body NotificacaoRequest request);
    }

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

