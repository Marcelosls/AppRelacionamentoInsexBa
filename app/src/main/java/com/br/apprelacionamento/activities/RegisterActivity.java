package com.br.apprelacionamento.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.api.ApiClient;
import com.br.apprelacionamento.api.ApiService;
import com.br.apprelacionamento.models.UserRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtFirstName, edtLastName, edtEmail, edtConfirmEmail,
            edtPassword, edtBirthDate;
    private Spinner spinnerGender;// Spinner de gênero
    private Button btnRegister;// Botão de registro
    //private String selectedGender;// Variável para armazenar o gênero selecionado

    // Configura a activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        edtConfirmEmail = findViewById(R.id.edtConfirmEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtBirthDate = findViewById(R.id.edtBirthDate);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnRegister = findViewById(R.id.btnRegister);



        // Configura DatePickerDialog para data de nascimento
        edtBirthDate.setInputType(InputType.TYPE_NULL); // Evita teclado
        edtBirthDate.setOnClickListener(v -> showDatePickerDialog());

        // Spinner de gênero
        setupGenderSpinner();

        btnRegister.setOnClickListener(v -> registerUser());
    }

    // Exibe o DatePickerDialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    edtBirthDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    // Configura o Spinner de gênero
    private void setupGenderSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //selectedGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Gênero padrão já setado
            }
        });
    }

    // Registra o usuário
    private void registerUser() {
        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String confirmEmail = edtConfirmEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String birthDate = edtBirthDate.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();

        CheckBox ckTermos = findViewById(R.id.ckTermos);
        CheckBox ckPrivacidade = findViewById(R.id.ckPrivacidade);
        CheckBox ckMarketing = findViewById(R.id.ckMarketing); // Opcional

        // Campos obrigatórios
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || confirmEmail.isEmpty()
                || password.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Emails diferentes
        if (!email.equals(confirmEmail)) {
            Toast.makeText(this, "Os e-mails não coincidem.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gênero não selecionado
        if (gender.equals("Selecione um Genero")) {
            Toast.makeText(this, "Por favor, selecione um gênero válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Checkboxes obrigatórios
        if (!ckTermos.isChecked() || !ckPrivacidade.isChecked()) {
            Toast.makeText(this, "Você deve aceitar os termos e a política de privacidade.", Toast.LENGTH_SHORT).show();
            return;
        }

        String formattedDate = formatDateToBackend(birthDate);
        if (formattedDate == null) {
            Toast.makeText(this, "Data de nascimento inválida..", Toast.LENGTH_SHORT).show();
            return;
        }

        String genderForBackend = convertGenderToBackend(gender);

        UserRequest userRequest = new UserRequest(
                firstName,
                lastName,
                password,
                email,
                formattedDate,
                genderForBackend,
                "Usuario"
        );

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.registerUser(userRequest);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Usuário registrado com sucesso!", Toast.LENGTH_LONG).show();
                    finish(); // fecha a tela
                } else {
                    Toast.makeText(RegisterActivity.this, "Erro ao registrar. Código: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Formata a data para o padrão do backend ano-mes-dia
    private String formatDateToBackend(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            return null;
        }
    }

    // Converte o gênero para o padrão do backend "Masculino" "Feminino" "Nao_Binario"
    private String convertGenderToBackend(String gender) {
        switch (gender) {
            case "Masculino":
                return "Masculino";
            case "Feminino":
                return "Feminino";
            case "Não-Binário":
                return "Nao_Binario";
            default:
                return "Outro";
        }
    }
}
