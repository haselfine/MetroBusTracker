package com.example.metrobustracker.Repository;

import com.example.metrobustracker.Service.MetroService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MetroRepo {

    private static final String url = "https://svc.metrotransit.org/NexTrip/";

    public MetroService mMetroService;

    public MetroRepo(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMetroService = retrofit.create(MetroService.class);
    }
}
