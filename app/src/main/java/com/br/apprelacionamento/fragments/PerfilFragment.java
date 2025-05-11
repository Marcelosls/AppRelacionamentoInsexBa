package com.br.apprelacionamento.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.activities.InteressesActivity;
import com.br.apprelacionamento.activities.EditarPerfilActivity;
import com.br.apprelacionamento.adapter.EnumConverter;
import com.br.apprelacionamento.adapter.InterestsAdapter;
import com.br.apprelacionamento.api.ApiClient;
import com.br.apprelacionamento.api.ApiInterface;
import com.br.apprelacionamento.models.ProfileResponse;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilFragment extends Fragment {

    private ShapeableImageView imageProfile;
    private TextView textNameAge, textGender, textEthnicity, textEducation,
            textMaritalStatus, textDesiredRelationship, textProfession, textBio;
    private RecyclerView recyclerViewInterests;
    private InterestsAdapter interestsAdapter;
    private List<String> interests = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        Button btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(v -> {
            // Inicia a Activity de edição de perfil
            Intent intent = new Intent(requireContext(), EditarPerfilActivity.class);
            startActivity(intent);
        });

        imageProfile = view.findViewById(R.id.imageProfile);
        textNameAge = view.findViewById(R.id.textNameAge);
        textGender = view.findViewById(R.id.textGender);
        textEthnicity = view.findViewById(R.id.textEthnicity);
        textEducation = view.findViewById(R.id.textEducation);
        textMaritalStatus = view.findViewById(R.id.textMaritalStatus);
        textDesiredRelationship = view.findViewById(R.id.textDesiredRelationship);
        textProfession = view.findViewById(R.id.textProfession);
        textBio = view.findViewById(R.id.textBio);

        recyclerViewInterests = view.findViewById(R.id.recyclerViewInterests);
        recyclerViewInterests.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        interestsAdapter = new InterestsAdapter(interests, interest -> {
            // Ao clicar em um interesse, abre a tela de edição
            Intent intent = new Intent(requireContext(), InteressesActivity.class);
            startActivity(intent);
        });

        recyclerViewInterests.setAdapter(interestsAdapter);

        carregarPerfil();

        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        carregarPerfil(); // aqui você faz a requisição GET para buscar os dados atualizados
    }


    private void carregarPerfil() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String token = preferences.getString("authToken", "");
        int userId = preferences.getInt("userId", -1);

        if (token.isEmpty() || userId == -1) return;

        ApiInterface api = ApiClient.getApiServiceWithAuth(requireContext());
        Call<ProfileResponse> call = api.getProfile("Bearer " + token, userId);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse profile = response.body();
                    preencherCampos(profile);
                } else {
                    Toast.makeText(getContext(), "Erro ao carregar perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void preencherCampos(ProfileResponse profile) {
        textNameAge.setText(profile.getFirstName() + " " + profile.getLastName() + ", " + profile.getAge());
        textGender.setText("Gênero: " + EnumConverter.convertGender(profile.getGender()));
        textEthnicity.setText("Etnia: " + EnumConverter.convertEthnicity(profile.getEthnicity()));
        textEducation.setText("Educação: " + EnumConverter.convertEducation(profile.getEducation()));
        textMaritalStatus.setText("Estado civil: " + EnumConverter.convertMaritalStatus(profile.getMaritalStatus()));
        textDesiredRelationship.setText("Deseja: " + EnumConverter.convertDesiredRelationship(profile.getDesiredRelationship()));
        textProfession.setText("Profissão: " + profile.getProfession());
        textBio.setText("Bio: " + profile.getBio());

        if (profile.getProfilePicture() != null) {
            // Se vier imagem em base64 ou bytes, você pode converter e exibir com Glide/Picasso
        } else {
            imageProfile.setImageResource(R.drawable.perfil_padrao);
        }

        interests.clear();
        interests.addAll(profile.getInterests());
        interestsAdapter.notifyDataSetChanged();
    }
}
