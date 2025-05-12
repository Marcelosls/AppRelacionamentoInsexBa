package com.br.apprelacionamento.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

    private static final int REQUEST_CODE_SELECT_IMAGE = 100;
    private static final int REQUEST_CODE_PERMISSIONS = 101;

    private Spinner relationshipSpinner, ethnicitySpinner, educationSpinner, maritalStatusSpinner;
    private EditText bioEditText;
    private Button saveButton, selectImageButton;
    private ImageView profileImageView;

    private ApiInterface api;
    private Uri selectedImageUri;
    private MultipartBody.Part profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        relationshipSpinner = findViewById(R.id.spinnerRelationship);
        ethnicitySpinner = findViewById(R.id.spinnerEthnicity);
        educationSpinner = findViewById(R.id.spinnerEducation);
        maritalStatusSpinner = findViewById(R.id.spinnerMaritalStatus);
        bioEditText = findViewById(R.id.edtBio);
        saveButton = findViewById(R.id.btnSave);
        selectImageButton = findViewById(R.id.btnSelectImage);
        profileImageView = findViewById(R.id.imgProfilePreview);

        api = ApiClient.getApiServiceWithAuth(ProfileCreationActivity.this);

        loadSpinners();

        selectImageButton.setOnClickListener(v -> requestImagePermission());

        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void loadSpinners() {
        relationshipSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.desired_relationship_array, android.R.layout.simple_spinner_item));
        ethnicitySpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.ethnicity_array, android.R.layout.simple_spinner_item));
        educationSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.education_array, android.R.layout.simple_spinner_item));
        maritalStatusSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.marital_status_array, android.R.layout.simple_spinner_item));
    }

    private void requestImagePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSIONS);
        } else {
            openImagePicker();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else {
            Toast.makeText(this, "Permissão negada para acessar imagens", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);
            profilePicture = prepareFilePart(selectedImageUri);
        }
    }

    private MultipartBody.Part prepareFilePart(Uri fileUri) {
        try {
            File file = new File(getRealPathFromUri(fileUri));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            return MultipartBody.Part.createFormData("profilePicture", file.getName(), requestFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getRealPathFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) return uri.getPath();
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String path = cursor.getString(idx);
        cursor.close();
        return path;
    }

    private void saveProfile() {
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = preferences.getString("authToken", null);

        if (token == null) {
            Toast.makeText(this, "Token de autenticação não encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        String relationship = relationshipSpinner.getSelectedItem().toString();
        String ethnicity = ethnicitySpinner.getSelectedItem().toString();
        String education = educationSpinner.getSelectedItem().toString();
        String maritalStatus = maritalStatusSpinner.getSelectedItem().toString();
        String bio = bioEditText.getText().toString().trim();

        if (relationship.contains("Selecione") || ethnicity.contains("Selecione") ||
                education.contains("Selecione") || maritalStatus.contains("Selecione")) {
            Toast.makeText(this, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody relationshipBody = toRequestBody(relationship);
        RequestBody ethnicityBody = toRequestBody(ethnicity);
        RequestBody educationBody = toRequestBody(education);
        RequestBody maritalStatusBody = toRequestBody(maritalStatus);
        RequestBody bioBody = toRequestBody(bio);
        RequestBody professionBody = toRequestBody("Profissão Exemplo");
        RequestBody ageBody = toRequestBody("25");
        RequestBody interestsBody = toRequestBody("teste");

        MultipartBody.Part picture = (profilePicture != null) ? profilePicture : getDefaultProfilePicture();

        Call<ResponseBody> call = api.createProfile(
                "Bearer " + token,
                ethnicityBody,
                educationBody,
                maritalStatusBody,
                relationshipBody,
                bioBody,
                professionBody,
                picture,
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

