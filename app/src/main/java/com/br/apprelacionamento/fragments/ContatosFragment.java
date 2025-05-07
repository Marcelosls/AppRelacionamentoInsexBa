package com.br.apprelacionamento.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.activities.ContactsActivity;

public class ContatosFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        // Adicionei um ID "btnAbrirContatos" no fragment_contatos.xml
        Button btnAbrirContatos = view.findViewById(R.id.btnAbrirContatos);
        btnAbrirContatos.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ContactsActivity.class);
            startActivity(intent);
        });

        return view;
    }
}