package com.br.apprelacionamento.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.adapter.InterestsAdapter;

import java.util.Arrays;
import java.util.List;

public class PerfilFragment extends Fragment {

    private TextView textFirstLastNameAge;
    private RecyclerView recyclerViewInterests;

    // Spinners
    private Spinner spinnerGender, spinnerEthnicity, spinnerEducation, spinnerMaritalStatus, spinnerDesiredRelationship;

    public PerfilFragment() {
        // Construtor vazio obrigatório
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Nome + idade
        textFirstLastNameAge = view.findViewById(R.id.textFirstLastNameAge);
        textFirstLastNameAge.setText("Marcelo Leal, 23");

        // Interesses
        recyclerViewInterests = view.findViewById(R.id.recyclerViewInterests);
        List<String> interesses = Arrays.asList("Cinema", "Leitura", "Esportes", "Tecnologia", "Música");
        recyclerViewInterests.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewInterests.setAdapter(new InterestsAdapter(interesses));

        // Inicializar Spinners
        spinnerGender = view.findViewById(R.id.spinnerGender);
        spinnerEthnicity = view.findViewById(R.id.spinnerEthnicity);
        spinnerEducation = view.findViewById(R.id.spinnerEducation);
        spinnerMaritalStatus = view.findViewById(R.id.spinnerMaritalStatus);
        spinnerDesiredRelationship = view.findViewById(R.id.spinnerDesiredRelationship);

        setupConfirmableSpinner(spinnerGender, R.array.gender_array);
        setupConfirmableSpinner(spinnerEthnicity, R.array.ethnicity_array);
        setupConfirmableSpinner(spinnerEducation, R.array.education_array);
        setupConfirmableSpinner(spinnerMaritalStatus, R.array.marital_status_array);
        setupConfirmableSpinner(spinnerDesiredRelationship, R.array.desired_relationship_array);

        return view;
    }

    private void setupConfirmableSpinner(Spinner spinner, int arrayResId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                arrayResId,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final int[] previousPosition = {spinner.getSelectedItemPosition()};
        final boolean[] isFirstSelection = {true};

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection[0]) {
                    isFirstSelection[0] = false;
                    previousPosition[0] = position;
                    return;
                }

                if (position == previousPosition[0]) return;

                new AlertDialog.Builder(requireContext())
                        .setTitle("Confirmar alteração")
                        .setMessage("Deseja alterar o valor para \"" + parent.getItemAtPosition(position) + "\"?")
                        .setPositiveButton("✔", (dialog, which) -> {
                            previousPosition[0] = position;
                        })
                        .setNegativeButton("✖", (dialog, which) -> {
                            spinner.setSelection(previousPosition[0]);
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
