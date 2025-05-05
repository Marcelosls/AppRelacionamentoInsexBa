package com.br.apprelacionamento.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private Context context;

    public TokenInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences preferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String token = preferences.getString("authToken", null);

        Request originalRequest = chain.request();

        if (token == null) {
            return chain.proceed(originalRequest);
        }

        Request modifiedRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(modifiedRequest);
    }
}

