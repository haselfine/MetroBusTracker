package com.example.metrobustracker.Service;

import android.text.Layout;

import com.example.metrobustracker.Model.Directions;
import com.example.metrobustracker.Model.Routes;
import com.example.metrobustracker.Model.Stops;
import com.example.metrobustracker.Model.TimepointDepartures;
import com.example.metrobustracker.Model.VehicleLocations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MetroService {

    @GET("Routes?format=json")
    Call<Routes[]> getRoutes();

    @GET("VehicleLocations/{ROUTE}?format=json")
    Call<VehicleLocations[]> getVehicleLocation(@Path ("ROUTE") String ROUTE);

    @GET("Directions/{ROUTE}?format=json")
    Call<Directions[]> getDirection(@Path ("ROUTE") String ROUTE);

    @GET("Stops/{ROUTE}/{DIRECTION}?format=json")
    Call<Stops[]> getStops(@Path ("ROUTE") String ROUTE, @Path ("DIRECTION") String DIRECTION);

    @GET("{ROUTE}/{DIRECTION}/{STOP}?format=json")
    Call<TimepointDepartures[]> getDepartures(@Path ("ROUTE") String ROUTE, @Path ("DIRECTION") String Direction, @Path("STOP") String Stop);
}
