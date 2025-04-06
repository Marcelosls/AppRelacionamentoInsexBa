package com.br.apprelacionamento;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CadastroActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private EditText edtEndereco;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        EditText edtPrimeiroNome = findViewById(R.id.edtPrimeiroNome);
        EditText edtSobrenome = findViewById(R.id.edtSobrenome);
        EditText edtDataNascimento = findViewById(R.id.edtDataNascimento);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtSenha = findViewById(R.id.edtSenha);
        edtEndereco = findViewById(R.id.edtEndereco);

        CheckBox ckTermos = findViewById(R.id.ckTermos);
        CheckBox ckPrivacidade = findViewById(R.id.ckPrivacidade);
        CheckBox ckMarketing = findViewById(R.id.ckMarketing);

        Button btnConfirmar = findViewById(R.id.btnConfirmar);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Aplica máscara automática de data ao campo de nascimento
        aplicarMascaraData(edtDataNascimento);

        // Quando tocar no campo de endereço, pedir localização
        edtEndereco.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                solicitarPermissaoLocalizacao();
            }
        });

        btnConfirmar.setOnClickListener(v -> {
            String nome = edtPrimeiroNome.getText().toString().trim();
            String sobrenome = edtSobrenome.getText().toString().trim();
            String dataNascimento = edtDataNascimento.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String senha = edtSenha.getText().toString().trim();
            String endereco = edtEndereco.getText().toString().trim();

            if (nome.isEmpty() || sobrenome.isEmpty() || dataNascimento.isEmpty()
                    || email.isEmpty() || senha.isEmpty() || endereco.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            } else if (!ckTermos.isChecked() || !ckPrivacidade.isChecked()) {
                Toast.makeText(this, "Você precisa aceitar os termos e a política de privacidade.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cadastro enviado com sucesso!", Toast.LENGTH_SHORT).show();
                // Aqui no futuro: enviar os dados para a API
            }
        });
    }


    private void solicitarPermissaoLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            obterLocalizacaoAtual();
        }
    }

    private void obterLocalizacaoAtual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Solicita uma nova leitura de localização
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setNumUpdates(1); // Só uma vez

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    preencherCidade(location);
                }
            }
        }, Looper.getMainLooper());
    }

    private void preencherCidade(Location location) {
        try {
            Log.d("LOCALIZACAO", "Latitude: " + location.getLatitude());
            Log.d("LOCALIZACAO", "Longitude: " + location.getLongitude());

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1);

            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);

                String cidade = address.getLocality();
                String subAdmin = address.getSubAdminArea();
                String admin = address.getAdminArea();

                Log.d("LOCALIZACAO", "Locality: " + cidade);
                Log.d("LOCALIZACAO", "SubAdminArea: " + subAdmin);
                Log.d("LOCALIZACAO", "AdminArea: " + admin);

                // Tenta preencher com prioridade para a cidade
                if (cidade != null && !cidade.isEmpty()) {
                    edtEndereco.setText(cidade);
                } else if (subAdmin != null && !subAdmin.isEmpty()) {
                    edtEndereco.setText(subAdmin);
                } else if (admin != null && !admin.isEmpty()) {
                    edtEndereco.setText(admin);
                } else {
                    Toast.makeText(this, "Cidade não disponível", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Cidade não encontrada", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao obter cidade", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obterLocalizacaoAtual();
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //metodo para aplicar mascara na data
    private void aplicarMascaraData(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating;
            private String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString().replaceAll("[^\\d]", "");

                StringBuilder mascara = new StringBuilder();
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }

                int length = str.length();
                int i = 0;

                while (i < length && i < 8) {
                    if (i == 2 || i == 4) mascara.append('/');
                    mascara.append(str.charAt(i));
                    i++;
                }

                // Preenche com "D", "M" e "A" se estiver vazio
                if (str.isEmpty()) {
                    mascara.append("DD/MM/AAAA".substring(mascara.length()));
                }

                isUpdating = true;
                editText.setText(mascara.toString());
                editText.setSelection(mascara.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


}


