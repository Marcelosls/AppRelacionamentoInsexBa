package com.br.apprelacionamento.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.br.apprelacionamento.R;

public class TelaPrincipalActivity extends AppCompatActivity {

    private TextView txtBemVindo;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        txtBemVindo = findViewById(R.id.txtBemVindo);
        btnLogout = findViewById(R.id.btnLogout);

        // Pegando nome completo salvo (vamos simular por enquanto)
        SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        String nome = prefs.getString("FIRST_NAME", "Usuário");
        String sobrenome = prefs.getString("LAST_NAME", "");

        txtBemVindo.setText("Bem-vindo, " + nome + " " + sobrenome + "!");

        btnLogout.setOnClickListener(v -> {
            // Limpa tudo e volta para o início
            prefs.edit().clear().apply();
            Intent i = new Intent(TelaPrincipalActivity.this, InicioActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
    }
}
