package com.br.apprelacionamento.activities;

import android.Manifest;
import android.content.Intent;
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
        setContentView(R.layout.activity_inicio);

        TextView textView = findViewById(R.id.txtNomeApp);

        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                Shader textShader = new LinearGradient(
                        0, 0, textView.getWidth(), 0,  // Horizontal: x0, y0, x1, y1
                        new int[]{
                                Color.parseColor("#D2B162"), // início
                                Color.parseColor("#A67C00"), // meio (tom mais escuro)
                                Color.parseColor("#D2B162")  // fim
                        },
                        new float[]{0f, 0.5f, 1f}, // Posições relativas das cores
                        Shader.TileMode.CLAMP
                );

                textView.getPaint().setShader(textShader);
                textView.invalidate(); // Redesenha com o shader
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

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                Log.d("FCM", "Token gerado: " + token);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InicioActivity.this, RegisterActivity.class));
            }
        });
    }
}
