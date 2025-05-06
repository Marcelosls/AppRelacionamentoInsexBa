package com.br.apprelacionamento.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.activities.MainNavigationActivity;
import com.br.apprelacionamento.api.ApiClient;
import com.br.apprelacionamento.api.ApiInterface;
import com.br.apprelacionamento.models.InterestsRequest;
import com.br.apprelacionamento.models.LoginRequest;
import com.br.apprelacionamento.models.LoginResponse;
import com.br.apprelacionamento.models.ProfileRequest;
import com.br.apprelacionamento.models.ProfileResponse;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.edtEmailLogin);
        passwordEditText = findViewById(R.id.edtSenhaLogin);
        loginButton = findViewById(R.id.btnEntrar);

        api = ApiClient.getApiServiceNoAuth();

        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<LoginResponse> call = api.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();

                    SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    int userId = extractUserIdFromToken(token);

                    preferences.edit()
                            .putString("authToken", token)
                            .putInt("userId", userId)
                            .apply();

                    enviarDadosPadraoSePerfilNaoExistir(token);

                    Log.d("LOGIN", "Token: " + token);
                    Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainNavigationActivity.class));
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarDadosPadraoSePerfilNaoExistir(String token) {
        ApiInterface authApi = ApiClient.getApiServiceWithAuth(LoginActivity.this); // Usa token

        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "ID do usuário não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        authApi.getProfile("Bearer " + token, userId).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    // Perfil ainda não criado → criar perfil padrão
                    criarPerfilPadrao(token);
                } else {
                    Log.d("LOGIN", "Perfil já existente.");
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("LOGIN", "Erro ao verificar perfil: " + t.getMessage());
            }
        });
    }

    private void criarPerfilPadrao(String token) {
        ApiInterface authApi = ApiClient.getApiServiceWithAuth(LoginActivity.this);

        MultipartBody.Part foto = getDefaultProfilePicture();

        Call<ResponseBody> call = authApi.createProfile(
                "Bearer " + token,
                toRequestBody("Outro"),
                toRequestBody("Ensino_Superior"),
                toRequestBody("Solteiro"),
                toRequestBody("Namoro"),
                toRequestBody("Oi, estou usando Encontro"),
                toRequestBody("Não informado"),
                foto,
                toRequestBody("1"),
                toRequestBody("falta cadastrar")
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("PERFIL", "Perfil criado com sucesso!");

                    // Agora envia os interesses padrão
                    List<String> defaultInterests = Arrays.asList("falta cadastrar");
                    InterestsRequest interestsRequest = new InterestsRequest(defaultInterests);

                    ApiInterface authApi = ApiClient.getApiServiceWithAuth(LoginActivity.this);
                    Call<Void> interestsCall = authApi.createInterests(interestsRequest);

                    interestsCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("INTERESSES", "Interesses cadastrados com sucesso!");
                            } else {
                                Log.e("INTERESSES", "Erro ao cadastrar interesses: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("INTERESSES", "Falha ao cadastrar interesses: " + t.getMessage());
                        }
                    });
                } else {
                    Log.e("PERFIL", "Erro ao criar perfil: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("PERFIL", "Falha na criação do perfil: " + t.getMessage());
            }
        });
    }


    private MultipartBody.Part getDefaultProfilePicture() {
        try {
            // Carrega o drawable como Bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.perfil_padrao);

            // Cria um arquivo temporário
            File file = new File(getCacheDir(), "perfil_padrao.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            // Cria o RequestBody e Multipart
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            return MultipartBody.Part.createFormData("profilePicture", file.getName(), requestFile);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }


    private int extractUserIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return -1;

            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(payload);

            return jsonObject.getInt("id"); // ou "sub", "userId", etc., dependendo do campo usado
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


}
