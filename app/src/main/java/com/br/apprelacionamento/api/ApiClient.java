package com.br.apprelacionamento.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofitNoAuth;
    private static Retrofit retrofitWithAuth;

    // Retrofit SEM autenticação (para login e registro)
    public static ApiInterface getApiServiceNoAuth() {
        if (retrofitNoAuth == null) {
            retrofitNoAuth = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitNoAuth.create(ApiInterface.class);
    }

    // Retrofit COM autenticação (para endpoints protegidos)
    public static ApiInterface getApiServiceWithAuth(Context context) {
        if (retrofitWithAuth == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new TokenInterceptor(context))
                    .build();

            retrofitWithAuth = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitWithAuth.create(ApiInterface.class);
    }

    private static class TokenInterceptor implements Interceptor {
        private final Context context;

        public TokenInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("authToken", null);

            Request request = chain.request();
            if (token != null) {
                request = request.newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
            }
            return chain.proceed(request);
        }
    }
}
