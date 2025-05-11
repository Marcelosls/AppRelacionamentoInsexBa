package com.br.apprelacionamento.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.br.apprelacionamento.R;
import com.br.apprelacionamento.api.ApiClient;
import com.br.apprelacionamento.api.ApiInterface;
import com.br.apprelacionamento.api.RetrofitClient;
import com.br.apprelacionamento.api.SharedPrefManager;
import com.br.apprelacionamento.models.InterestsRequest;
import com.br.apprelacionamento.models.ProfileResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InteressesActivity extends AppCompatActivity {

    private CheckBox checkboxBelezaNatural, checkboxAtividadeFisica, checkboxDocumentarios, checkboxCulinaria,
            checkboxFilmes, checkboxSeries, checkboxFestasRua, checkboxMomentosCasa, checkboxViagens;
    private Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesses);

        // Inicializando os CheckBoxes
        checkboxBelezaNatural = findViewById(R.id.checkboxBelezaNatural);
        checkboxAtividadeFisica = findViewById(R.id.checkboxAtividadeFisica);
        checkboxDocumentarios = findViewById(R.id.checkboxDocumentarios);
        checkboxCulinaria = findViewById(R.id.checkboxCulinaria);
        checkboxFilmes = findViewById(R.id.checkboxFilmes);
        checkboxSeries = findViewById(R.id.checkboxSeries);
        checkboxFestasRua = findViewById(R.id.checkboxFestasRua);
        checkboxMomentosCasa = findViewById(R.id.checkboxMomentosCasa);
        checkboxViagens = findViewById(R.id.checkboxViagens);

        btnContinuar = findViewById(R.id.btnContinuar);

        btnContinuar.setOnClickListener(v -> {
            List<String> selectedInterests = new ArrayList<>();
            if (checkboxBelezaNatural.isChecked()) selectedInterests.add("Beleza Natural");
            if (checkboxAtividadeFisica.isChecked()) selectedInterests.add("Atividade física");
            if (checkboxDocumentarios.isChecked()) selectedInterests.add("Documentários");
            if (checkboxCulinaria.isChecked()) selectedInterests.add("Culinária");
            if (checkboxFilmes.isChecked()) selectedInterests.add("Filmes");
            if (checkboxSeries.isChecked()) selectedInterests.add("Séries");
            if (checkboxFestasRua.isChecked()) selectedInterests.add("Festas na rua");
            if (checkboxMomentosCasa.isChecked()) selectedInterests.add("Momentos em casa");
            if (checkboxViagens.isChecked()) selectedInterests.add("Viagens");

            if (selectedInterests.isEmpty()) {
                selectedInterests.add("Falta cadastrar"); // Interesse padrão
            }

            // Criando o objeto InterestsRequest com os interesses selecionados
            InterestsRequest interestsRequest = new InterestsRequest(selectedInterests);

            // Enviando os interesses para o servidor via Retrofit
            sendInterests(interestsRequest);
        });
    }

    private void sendInterests(InterestsRequest interestsRequest) {
        // Usando o ApiClient para fazer a requisição
        ApiClient.getApiServiceWithAuth(this)
                .createInterests(interestsRequest)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(InteressesActivity.this, "Interesses cadastrados com sucesso!", Toast.LENGTH_SHORT).show();
                            finish(); // Fechar a atividade após sucesso
                        } else {
                            Toast.makeText(InteressesActivity.this, "Falha ao cadastrar interesses", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(InteressesActivity.this, "Erro de comunicação", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

