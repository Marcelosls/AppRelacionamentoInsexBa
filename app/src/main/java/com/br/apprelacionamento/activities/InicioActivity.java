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
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.br.apprelacionamento.activities.LoginActivity;
import com.br.apprelacionamento.R;
import com.google.firebase.messaging.FirebaseMessaging;


public class InicioActivity extends AppCompatActivity {

    private Button btnLogin, btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica se o token de autenticação está salvo
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = preferences.getString("authToken", null);

        if (token != null && !token.isEmpty()) {
            // Usuário já está autenticado → vai direto pra tela principal
            Intent intent = new Intent(InicioActivity.this, MainNavigationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            // Usuário não está logado → mostra tela de login/cadastro
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

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(InicioActivity.this, LoginActivity.class));
                }
            });

            btnCadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(InicioActivity.this, RegisterActivity.class));
                }
            });

            // Aqui é o token do Firebase Cloud Messaging (FCM), pode continuar usando esse nome
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String fcmToken = task.getResult(); // ← troquei o nome da variável para evitar conflito
                    Log.d("FCM", "Token gerado: " + fcmToken);
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

}
