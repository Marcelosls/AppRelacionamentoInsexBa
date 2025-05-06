package com.br.apprelacionamento.models;

import java.util.List;

public class InterestsRequest {
    private List<String> interests;

    public InterestsRequest(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}

