package com.br.apprelacionamento.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.adapters.ChatAdapter;

import com.br.apprelacionamento.api.ApiService;
import com.br.apprelacionamento.api.ContactsClient;
import com.br.apprelacionamento.models.ChatMessage;
import com.br.apprelacionamento.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText inputMessage;
    private FrameLayout layoutSend;
    private ProgressBar progressBar;
    private ChatAdapter chatAdapter;
    private User currentUser;
    private int receiverId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();

        currentUser = getUserData();
        receiverId = getIntent().getIntExtra("receiverId", -1); // Recebe o ID do destinatário
        setupRecyclerView(currentUser.getId());

        loadMessages();

        layoutSend.setOnClickListener(v -> {
            String messageText = inputMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(messageText)) {
                sendMessage(messageText);
            } else {
                Toast.makeText(this, "Por favor, digite uma mensagem.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        inputMessage = findViewById(R.id.inputMessage);
        layoutSend = findViewById(R.id.layoutSend);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView(int senderId) {
        chatAdapter = new ChatAdapter(senderId);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);
    }

    private void loadMessages() {
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = ContactsClient.getClient().create(ApiService.class);
        int senderId = currentUser.getId();

        apiService.getMessages(senderId, receiverId).enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    chatAdapter.setMessages(response.body());
                    chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                } else {
                    Toast.makeText(ChatActivity.this, "Falha ao carregar as mensagens.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, "Erro ao conectar ao servidor.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String messageText) {
        ApiService apiService = ContactsClient.getClient().create(ApiService.class);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId(currentUser.getId());
        chatMessage.setReceiverId(receiverId);
        chatMessage.setMessage(messageText);

        apiService.sendMessage(chatMessage).enqueue(new Callback<ChatMessage>() {
            @Override
            public void onResponse(Call<ChatMessage> call, Response<ChatMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatAdapter.addMessage(response.body());
                    inputMessage.setText("");
                    chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                } else {
                    Toast.makeText(ChatActivity.this, "Falha ao enviar a mensagem.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatMessage> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Erro ao enviar a mensagem.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private User getUserData() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);

        int userId = prefs.getInt("userId", -1);
        String firstName = prefs.getString("firstName", "");
        String lastName = prefs.getString("lastName", "");
        String email = prefs.getString("email", "");

        User user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        // Caso queira adcionar um setter para o email no futuro, descomente a linha abaixo
        // user.setEmail(email);

        return user;
    }
}