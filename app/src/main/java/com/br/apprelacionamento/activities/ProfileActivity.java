package com.br.apprelacionamento.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.ArrayList;

import com.br.apprelacionamento.api.ApiClient;
import com.br.apprelacionamento.api.ApiInterface;
import com.br.apprelacionamento.models.MatchedProfileDTO;
import com.br.apprelacionamento.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView nameView, ageView, bioView, professionView, matchView;
    private Button likeButton, dislikeButton;

    private List<MatchedProfileDTO> profiles;
    private int currentIndex = 0;
    private ApiInterface api;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profileImage);
        nameView = findViewById(R.id.nameView);
        ageView = findViewById(R.id.ageView);
        bioView = findViewById(R.id.bioView);
        professionView = findViewById(R.id.professionView);
        likeButton = findViewById(R.id.likeButton);
        dislikeButton = findViewById(R.id.dislikeButton);

        api = ApiClient.getClient().create(ApiInterface.class);
        token = getTokenFromPrefs();

        carregarPerfis();

        likeButton.setOnClickListener(v -> curtirPerfil());
        dislikeButton.setOnClickListener(v -> recusarPerfil());
    }

    private String getTokenFromPrefs() {
        SharedPreferences prefs = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        return "Bearer " + prefs.getString("jwt", "");
    }

    private void carregarPerfis() {
        api.getDiscoverProfiles(token).enqueue(new Callback<List<MatchedProfileDTO>>() {
            @Override
            public void onResponse(Call<List<MatchedProfileDTO>> call, Response<List<MatchedProfileDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    profiles = response.body();
                    if (!profiles.isEmpty()) {
                        exibirPerfilAtual();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Nenhum perfil disponível", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Erro ao carregar perfis", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<MatchedProfileDTO>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Falha na conexão", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void exibirPerfilAtual() {
        MatchedProfileDTO perfil = profiles.get(currentIndex);
        nameView.setText(perfil.getFirstName() + " " + perfil.getLastName());
        ageView.setText(String.valueOf(perfil.getAge()));
        bioView.setText(perfil.getBio());
        professionView.setText(perfil.getProfession());

        if (perfil.getProfilePictureBase64() != null) {
            byte[] decodedBytes = Base64.decode(perfil.getProfilePictureBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            profileImage.setImageBitmap(bitmap);
        } else {
            profileImage.setImageResource(R.drawable.default_image);
        }

        String encodedImage = perfil.getProfilePictureBase64();
        if (encodedImage != null) {
            storeUserImage(encodedImage);
        }
    }

    private void storeUserImage(String encodedImage) {
        SharedPreferences prefs = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userImage", encodedImage);
        editor.apply();
    }

    public String encodeImageToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void curtirPerfil() {
        MatchedProfileDTO perfil = profiles.get(currentIndex);
        api.curtirPerfil(token, perfil.getUserId()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    abrirTelaMatch(perfil);
                } else {
                    mostrarProximoPerfil();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Erro ao curtir perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recusarPerfil() {
        MatchedProfileDTO perfil = profiles.get(currentIndex);
        api.recusarPerfil("Bearer " + token, perfil.getUserId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mostrarProximoPerfil();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Erro ao recusar perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarProximoPerfil() {
        currentIndex++;
        if (currentIndex < profiles.size()) {
            exibirPerfilAtual();
        } else {
            Toast.makeText(this, "Você viu todos os perfis!", Toast.LENGTH_LONG).show();
            currentIndex = 0;
        }
    }

    private void abrirTelaMatch(MatchedProfileDTO perfil) {
        Intent intent = new Intent(this, MatchActivity.class);
        intent.putExtra("matchedUserName", perfil.getFirstName());
        intent.putExtra("matchedUserImage", perfil.getProfilePictureBase64());
        startActivity(intent);
    }
}
