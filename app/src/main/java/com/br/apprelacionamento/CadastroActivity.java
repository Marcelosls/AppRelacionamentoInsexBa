package com.br.apprelacionamento;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Ligando elementos da tela com o código
        EditText edtPrimeiroNome = findViewById(R.id.edtPrimeiroNome);
        EditText edtSobrenome = findViewById(R.id.edtSobrenome);
        EditText edtDataNascimento = findViewById(R.id.edtDataNascimento);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtSenha = findViewById(R.id.edtSenha);
        EditText edtEndereco = findViewById(R.id.edtEndereco);

        CheckBox ckTermos = findViewById(R.id.ckTermos);
        CheckBox ckPrivacidade = findViewById(R.id.ckPrivacidade);
        CheckBox ckMarketing = findViewById(R.id.ckMarketing); // Esse pode ser opcional

        Button btnConfirmar = findViewById(R.id.btnConfirmar);

        // Ação do botão
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
}
