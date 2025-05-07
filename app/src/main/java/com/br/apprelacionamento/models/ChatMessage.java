package com.br.apprelacionamento.models;

public class ChatMessage {
    private int id;
    private int senderId;
    private int receiverId;
    private String message;
    private String timestamp;

    public ChatMessage(){

    }

    // Getters
    public int getId() {
        return id;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters (opcional, se quiser modificar os dados depois)
    public void setId(int id) {
        this.id = id;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}