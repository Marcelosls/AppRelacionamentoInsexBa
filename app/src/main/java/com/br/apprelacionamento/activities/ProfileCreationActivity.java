package com.br.apprelacionamento.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.api.ApiClient;
import com.br.apprelacionamento.api.ApiInterface;

import android.view.View;
import android.widget.ArrayAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileCreationActivity extends AppCompatActivity {

    private Spinner relationshipSpinner, ethnicitySpinner, educationSpinner, maritalStatusSpinner;
    private EditText bioEditText;
    private Button saveButton;
    private ApiInterface api;
    private MultipartBody.Part profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        // Encontrar as views
        relationshipSpinner = findViewById(R.id.spinnerRelationship);
        ethnicitySpinner = findViewById(R.id.spinnerEthnicity);
        educationSpinner = findViewById(R.id.spinnerEducation);
        maritalStatusSpinner = findViewById(R.id.spinnerMaritalStatus);
        bioEditText = findViewById(R.id.edtBio);
        saveButton = findViewById(R.id.btnSave);

        api = ApiClient.getApiServiceWithAuth(ProfileCreationActivity.this);


        // ✅ Inicialize a imagem padrão
        profilePicture = getDefaultProfilePicture();

        // Carregar os spinners com as opções do strings.xml
        ArrayAdapter<CharSequence> relationshipAdapter = ArrayAdapter.createFromResource(this,
                R.array.desired_relationship_array, android.R.layout.simple_spinner_item);
        relationshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relationshipSpinner.setAdapter(relationshipAdapter);

        ArrayAdapter<CharSequence> ethnicityAdapter = ArrayAdapter.createFromResource(this,
                R.array.ethnicity_array, android.R.layout.simple_spinner_item);
        ethnicityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ethnicitySpinner.setAdapter(ethnicityAdapter);

        ArrayAdapter<CharSequence> educationAdapter = ArrayAdapter.createFromResource(this,
                R.array.education_array, android.R.layout.simple_spinner_item);
        educationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        educationSpinner.setAdapter(educationAdapter);

        ArrayAdapter<CharSequence> maritalStatusAdapter = ArrayAdapter.createFromResource(this,
                R.array.marital_status_array, android.R.layout.simple_spinner_item);
        maritalStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maritalStatusSpinner.setAdapter(maritalStatusAdapter);

        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = preferences.getString("authToken", null);

        if (token == null) {
            Toast.makeText(this, "Token de autenticação não encontrado. Por favor, faça o login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String relationship = relationshipSpinner.getSelectedItem().toString();
        String ethnicity = ethnicitySpinner.getSelectedItem().toString();
        String education = educationSpinner.getSelectedItem().toString();
        String maritalStatus = maritalStatusSpinner.getSelectedItem().toString();
        String bio = bioEditText.getText().toString().trim();

        if (relationship.equals("Selecione um o relacionamento desejado") ||
                ethnicity.equals("Selecione uma etnia") ||
                education.equals("Selecione uma escolaridade") ||
                maritalStatus.equals("Selecione um estado civil")) {
            Toast.makeText(this, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody relationshipBody = toRequestBody(relationship);
        RequestBody ethnicityBody = toRequestBody(ethnicity);
        RequestBody educationBody = toRequestBody(education);
        RequestBody maritalStatusBody = toRequestBody(maritalStatus);
        RequestBody bioBody = toRequestBody(bio);
        RequestBody professionBody = toRequestBody("Profissão Exemplo");  // Ajuste conforme necessário
        RequestBody ageBody = toRequestBody("25");  // Ajuste conforme necessário
        RequestBody interestsBody = toRequestBody("teste");  // Ajuste conforme necessário

        MultipartBody.Part profilePicturePart = MultipartBody.Part.createFormData(
                "profilePicture",
                "profile_picture.jpg",
                RequestBody.create(MediaType.parse("image/*"), profilePicture.toString())
        );

        Call<ResponseBody> call = api.createProfile(
                "Bearer " + token,
                ethnicityBody,
                educationBody,
                maritalStatusBody,
                relationshipBody,
                bioBody,
                professionBody,
                profilePicturePart,
                ageBody,
                interestsBody
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileCreationActivity.this, "Perfil criado com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileCreationActivity.this, InteressesActivity.class));
                    finish();
                } else {
                    Toast.makeText(ProfileCreationActivity.this, "Erro ao criar perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileCreationActivity.this, "Falha ao criar perfil: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MultipartBody.Part getDefaultProfilePicture() {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.perfil_padrao);
            File file = new File(getCacheDir(), "perfil_padrao.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

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
}
