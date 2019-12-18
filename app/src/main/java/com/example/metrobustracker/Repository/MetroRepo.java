package com.example.metrobustracker.Repository;

import com.example.metrobustracker.Service.MetroService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MetroRepo { //I used retrofit because I feel more comfortable with it. Newer version will use volley for caching

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
