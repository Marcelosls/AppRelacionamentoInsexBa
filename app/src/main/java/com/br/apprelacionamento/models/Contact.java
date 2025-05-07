package com.br.apprelacionamento.models;

import com.google.gson.annotations.SerializedName;

public class Contact {

    private String id, name, image;

    @SerializedName("liked_by_current_user")
    private boolean likedByCurrentUser;

    @SerializedName("liked_current_user")
    private boolean likedCurrentUser;

    @SerializedName("sender_id")
    private String senderId;

    @SerializedName("receiver_id")
    private String receiverId;

    @SerializedName("status")
    private String status;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public boolean isLikedCurrentUser() {
        return likedCurrentUser;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getStatus() {
        return status;
    }
}
