package com.br.apprelacionamento.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.activities.ChatActivity;
import com.br.apprelacionamento.models.Contact;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private Context context;
    private List<Contact> contactList;
    private OnContactClickListener onContactClickListener;

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    public ContactsAdapter(Context context, List<Contact> contactList, OnContactClickListener listener) {
        this.context = context;
        this.contactList = contactList;
        this.onContactClickListener = listener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.nameTextView.setText(contact.getName());

        // Configura o clique no item de contato
        holder.itemView.setOnClickListener(v -> {
            // Navega para o ChatActivity, passando os IDs do remetente e destinatário
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("SENDER_ID", contact.getSenderId()); // O ID do usuário logado
            intent.putExtra("RECEIVER_ID", contact.getReceiverId()); // O ID do contato

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView profileImageView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.contactName);
            profileImageView = itemView.findViewById(R.id.contactImage);
        }
    }
}
