package com.br.apprelacionamento.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.api.ApiInterface;
import com.br.apprelacionamento.api.ContactsClient;
import com.br.apprelacionamento.adapters.ContactsAdapter;
import com.br.apprelacionamento.models.Contact;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ContatosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        // Inicializa o RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Chama a API para pegar os contatos
        ApiInterface apiService = ContactsClient.getClient().create(ApiInterface.class);

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
                    adapter = new ContactsAdapter(getActivity(), matchedContacts, contact -> {
                        // Ação quando um item da lista for clicado
                        Toast.makeText(getActivity(), "Clicou em " + contact.getName(), Toast.LENGTH_SHORT).show();
                    });

                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro ao carregar contatos", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
