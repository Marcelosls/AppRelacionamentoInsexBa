package com.br.apprelacionamento.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.br.apprelacionamento.R;
import com.br.apprelacionamento.fragments.ContatosFragment;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);  // Certifique-se de que o layout da Activity tem um FrameLayout

        // Verifica se o fragment já foi adicionado antes
        if (savedInstanceState == null) {
            // Adiciona o ContatosFragment ao FrameLayout da Activity
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ContatosFragment())  // "fragment_container" é o ID do FrameLayout
                    .commit();
        }
    }
}
