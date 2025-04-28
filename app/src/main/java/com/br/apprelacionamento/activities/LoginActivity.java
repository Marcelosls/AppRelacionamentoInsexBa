package com.br.apprelacionamento.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.api.AuthenticationDTO;
import com.br.apprelacionamento.api.LoginResponseDTO;
import com.br.apprelacionamento.fragments.PerfilFragment;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtSenha;
    private Button btnLogin;

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final String BASE_URL = "http://10.0.2.2:8080/auth/login"; // Altere aqui

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtSenha = findViewById(R.id.edtSenhaLogin);
        btnLogin = findViewById(R.id.btnEntrar);

        btnLogin.setOnClickListener(v -> fazerLogin());
    }

    private void fazerLogin() {
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        AuthenticationDTO auth = new AuthenticationDTO(email, senha);
        String json = gson.toJson(auth);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json
                );

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Erro na conexão com a API", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    LoginResponseDTO resposta = gson.fromJson(response.body().string(), LoginResponseDTO.class);

                    // Salva token localmente
                    SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
                    prefs.edit().putString("TOKEN", resposta.getToken()).apply();

                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainNavigationActivity.class));
                        finish();
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
