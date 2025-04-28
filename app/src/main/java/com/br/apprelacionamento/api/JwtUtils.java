package com.br.apprelacionamento.api;

import android.util.Base64;
import org.json.JSONObject;

public class JwtUtils {
    public static int getUserIdFromToken(String token) {
        try {
            String[] parts = token.split("\\."); // Header, Payload, Signature
            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
            JSONObject jsonObject = new JSONObject(payload);

            // Pega o campo "sub" que é o ID do usuário
            return jsonObject.getInt("userId");
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Retorna erro
        }
    }
}

