package com.example.metrobustracker.Service;

import com.example.metrobustracker.Model.Routes;
import com.example.metrobustracker.Model.VehicleLocations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MetroService {

    @GET("Routes?format=json")
    Call<Routes[]> getRoutes();

    @GET("VehicleLocations/{ROUTE}?format=json")
    Call<VehicleLocations[]> getVehicleLocation(@Path ("ROUTE") String ROUTE);
}
