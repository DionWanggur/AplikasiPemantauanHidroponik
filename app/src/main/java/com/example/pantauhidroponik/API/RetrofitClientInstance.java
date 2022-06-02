package com.example.pantauhidroponik.API;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl("https://wspemantauanhidroponik.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory
                            .create(new GsonBuilder()
                                    .setLenient()
                                    .create()))
                    .build();
        }
        return retrofit;
    }
}
