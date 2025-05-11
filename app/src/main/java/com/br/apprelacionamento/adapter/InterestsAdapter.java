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
    private OnItemClickListener listener;

    // Interface para o clique
    public interface OnItemClickListener {
        void onItemClick(String interest);
    }

    // Construtor com listener
    public InterestsAdapter(List<String> interests, OnItemClickListener listener) {
        this.interests = interests;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textInterest;

        public ViewHolder(View itemView) {
            super(itemView);
            textInterest = itemView.findViewById(R.id.textInterest);
        }

        public void bind(String interest, OnItemClickListener listener) {
            textInterest.setText(interest);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(interest);
                }
            });
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
        holder.bind(interests.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }
}
