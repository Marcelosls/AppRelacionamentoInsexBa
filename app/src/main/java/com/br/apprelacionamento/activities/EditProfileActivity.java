package com.br.apprelacionamento.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.adapter.FileUtils;
import com.br.apprelacionamento.api.ApiClient;
import com.br.apprelacionamento.api.ApiInterface;
import com.br.apprelacionamento.api.SharedPrefManager;
import com.br.apprelacionamento.models.ProfileRequest;
import com.br.apprelacionamento.models.ProfileResponse;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView imageProfile;
    private EditText editFirstName, editLastName, editAge, editProfession, editBio;
    private Spinner spinnerGender, spinnerEthnicity, spinnerEducation, spinnerMaritalStatus, spinnerDesiredRelationship;
    private Uri imageUri;

    private static final int IMAGE_PICK_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imageProfile = findViewById(R.id.imageProfile);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editAge = findViewById(R.id.editAge);
        editProfession = findViewById(R.id.editProfession);
        editBio = findViewById(R.id.editBio);

        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerEthnicity = findViewById(R.id.spinnerEthnicity);
        spinnerEducation = findViewById(R.id.spinnerEducation);
        spinnerMaritalStatus = findViewById(R.id.spinnerMaritalStatus);
        spinnerDesiredRelationship = findViewById(R.id.spinnerDesiredRelationship);

        // Carregar opções dos spinners
        loadEnumSpinners();

        // Escolher imagem
        findViewById(R.id.btnChooseImage).setOnClickListener(v -> pickImage());

        // Salvar perfil
        findViewById(R.id.btnSaveProfile).setOnClickListener(v -> updateProfile());

        // Ir para tela de interesses
        findViewById(R.id.btnEditInterests).setOnClickListener(v -> startActivity(new Intent(this, InteressesActivity.class)));

        // Pré-carrega dados do usuário
        loadUserProfile();
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imageProfile.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadEnumSpinners() {
        // Exemplo para um spinner
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Masculino", "Feminino", "Nao_Binario"});
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        ArrayAdapter<String> adapterDesiredRelationship = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Namoro", "Casamento", "Relação_Aberta", "Amizade"});
        adapterDesiredRelationship.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDesiredRelationship.setAdapter(adapterDesiredRelationship);

        ArrayAdapter<String> adapterEducation = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Ensino_Fundamental", "Ensino_Médio", "Ensino_Superior", "Pós-graduação"});
        adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEducation.setAdapter(adapterEducation);

        ArrayAdapter<String> adapterEthnicity = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Branco", "Preto", "Pardo", "Asiatico", "Indigena", "Outro"});
        adapterEthnicity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEthnicity.setAdapter(adapterEthnicity);

        ArrayAdapter<String> adapterMaritalStatus = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Solteiro", "Casado", "Divorciado", "Viuvo"});
        adapterMaritalStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaritalStatus.setAdapter(adapterMaritalStatus);
    }

    private void loadUserProfile() {
        int userId = SharedPrefManager.getInstance(this).getUserId();
        ApiInterface api = ApiClient.getApiServiceWithAuth(this);

        api.getProfile("Bearer " + SharedPrefManager.getInstance(this).getAuthToken(), userId)
                .enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ProfileResponse profile = response.body();
                            // Preencher os campos...
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {
                        Toast.makeText(EditProfileActivity.this, "Erro ao carregar perfil", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateProfile() {
        // Campos básicos
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String bio = editBio.getText().toString().trim();
        String profession = editProfession.getText().toString().trim();
        String ageStr = editAge.getText().toString().trim();
        int age = TextUtils.isDigitsOnly(ageStr) && !ageStr.isEmpty() ? Integer.parseInt(ageStr) : 0;

        // Enums (convertidos para string do Enum exato)
        String gender = spinnerGender.getSelectedItem().toString();
        String ethnicity = spinnerEthnicity.getSelectedItem().toString();
        String education = spinnerEducation.getSelectedItem().toString();
        String maritalStatus = spinnerMaritalStatus.getSelectedItem().toString();
        String desiredRelationship = spinnerDesiredRelationship.getSelectedItem().toString();

        // Criar o JSON dos interesses
        String[] interestsArray = new String[] {"teste"}; // Exemplo de interesse
        String interestsJson = new Gson().toJson(interestsArray);

        // Criar a parte do formulário para interesses com JSON
        RequestBody interestsBody = RequestBody.create(
                MediaType.parse("application/json"),
                interestsJson
        );

        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setInterests(interestsBody);

        // Criar partes do formulário (textos)
        RequestBody firstNameBody = createPartFromString(firstName);
        RequestBody lastNameBody = createPartFromString(lastName);
        RequestBody bioBody = createPartFromString(bio);
        RequestBody professionBody = createPartFromString(profession);
        RequestBody ageBody = createPartFromString(String.valueOf(age));
        RequestBody genderBody = createPartFromString(gender);
        RequestBody ethnicityBody = createPartFromString(ethnicity);
        RequestBody educationBody = createPartFromString(education);
        RequestBody maritalStatusBody = createPartFromString(maritalStatus);
        RequestBody desiredRelationshipBody = createPartFromString(desiredRelationship);

        // Criar Multipart da imagem (se houver)
        MultipartBody.Part profilePicturePart = null;
        if (imageUri != null) {
            File file = FileUtils.getFileFromUri(this, imageUri);
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
            profilePicturePart = MultipartBody.Part.createFormData("profilePicture", file.getName(), requestFile);
        }

        // Enviar via Retrofit
        ApiInterface api = ApiClient.getApiServiceWithAuth(this);

        Call<Void> call = api.updateProfile(
                "Bearer " + SharedPrefManager.getInstance(this).getAuthToken(),
                firstNameBody, lastNameBody, ageBody, genderBody, ethnicityBody,
                educationBody, maritalStatusBody, desiredRelationshipBody,
                bioBody, professionBody, interestsBody, profilePicturePart
        );

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Perfil atualizado!", Toast.LENGTH_SHORT).show();
                    finish(); // fecha a activity
                } else {
                    Toast.makeText(EditProfileActivity.this, "Erro ao salvar perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Falha na rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RequestBody createPartFromString(String value) {
        return RequestBody.create(MultipartBody.FORM, value != null ? value : "");
    }
}

