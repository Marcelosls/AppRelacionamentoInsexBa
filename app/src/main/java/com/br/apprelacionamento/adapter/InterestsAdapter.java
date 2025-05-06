package com.br.apprelacionamento.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.br.apprelacionamento.R;

import java.util.List;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.ViewHolder> {

    private List<String> interests;

    public InterestsAdapter(List<String> interests) {
        this.interests = interests;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textInterest;

        public ViewHolder(View itemView) {
            super(itemView);
            textInterest = itemView.findViewById(R.id.textInterest);
        }
    }

    @NonNull
    @Override
    public InterestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textInterest.setText(interests.get(position));
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }
}

