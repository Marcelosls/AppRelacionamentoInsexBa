package com.br.apprelacionamento.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.api.ApiClient;
import com.br.apprelacionamento.api.ApiService;
import com.br.apprelacionamento.api.ContactsClient;
import com.br.apprelacionamento.models.Contact;
import com.br.apprelacionamento.adapters.ContactsAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // Inicialize o RecyclerView
        recyclerView = findViewById(R.id.recyclerViewContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Chamada para a API
        ApiService apiService = ContactsClient.getClient().create(ApiService.class);

        apiService.getMatchedContacts().enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Contact> allContacts = response.body();
                    List<Contact> matchedContacts = new ArrayList<>();

                    // Filtra os contatos que deram match
                    for (Contact contact : allContacts) {
                        if (contact.isLikedByCurrentUser() && contact.isLikedCurrentUser()) {
                            matchedContacts.add(contact);
                        }
                    }

                    // Configura o adaptador
                    adapter = new ContactsAdapter(ContactsActivity.this, matchedContacts, contact -> {
                        // Ação quando um item da lista é clicado
                        Toast.makeText(ContactsActivity.this, "Clicou em " + contact.getName(), Toast.LENGTH_SHORT).show();
                    });

                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Toast.makeText(ContactsActivity.this, "Erro ao carregar contatos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
