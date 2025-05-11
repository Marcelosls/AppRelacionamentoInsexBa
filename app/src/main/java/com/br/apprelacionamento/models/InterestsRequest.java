package com.br.apprelacionamento.models;

import java.util.List;

public class InterestsRequest {
    private List<String> interests;

    // Construtor para inicializar a lista de interesses
    public InterestsRequest(List<String> interests) {
        this.interests = interests;
    }

    // Getter para a lista de interesses
    public List<String> getInterests() {
        return interests;
    }

    // Setter para a lista de interesses
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
