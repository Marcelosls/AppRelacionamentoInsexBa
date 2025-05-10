package com.br.apprelacionamento.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.br.apprelacionamento.R;

public class MatchActivity extends AppCompatActivity {

    private ImageView userImage, matchImage;
    private TextView matchText;
    private Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        userImage = findViewById(R.id.userImage);
        matchImage = findViewById(R.id.matchImage);
        matchText = findViewById(R.id.matchText);
        btnContinuar = findViewById(R.id.btnContinuar);

        // Recebendo as imagens em Base64 via Intent
        String base64User = getIntent().getStringExtra("userImage");
        String base64Match = getIntent().getStringExtra("matchImage");

        if (base64User != null) {
            userImage.setImageBitmap(decodeBase64ToBitmap(base64User));
        }

        if (base64Match != null) {
            matchImage.setImageBitmap(decodeBase64ToBitmap(base64Match));
        }

        // Animações
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale_in);

        matchText.startAnimation(fadeIn);
        userImage.startAnimation(scale);
        matchImage.startAnimation(scale);

        btnContinuar.setOnClickListener(v -> {
            finish(); // ou abrir a próxima tela
        });
    }

    private Bitmap decodeBase64ToBitmap(String base64) {
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}

