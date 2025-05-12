package com.br.apprelacionamento.api;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager instance;
    private SharedPreferences sharedPreferences;
    private Context context;

    private static final String SHARED_PREF_NAME = "AppPrefs"; // Nome das preferências
    private static final String KEY_TOKEN = "authToken"; // Chave para o token
    private static final String KEY_USER_ID = "userId"; // Chave para o userId

    private SharedPrefManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveAuthData(String token, int userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1); // Valor padrão -1
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
