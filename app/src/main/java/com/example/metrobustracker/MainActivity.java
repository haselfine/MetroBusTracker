package com.example.metrobustracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.metrobustracker.Model.Directions;
import com.example.metrobustracker.Model.Routes;
import com.example.metrobustracker.Model.Stops;
import com.example.metrobustracker.Model.TimepointDepartures;
import com.example.metrobustracker.Model.VehicleLocations;
import com.example.metrobustracker.Repository.MetroRepo;
import com.example.metrobustracker.Service.MetroService;
import com.google.android.gms.maps.GoogleMap;

import java.sql.Time;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationBar.NavigationListener,
        DeparturesFragment.DepartureListener,
        WhereIsMyBusFragment.WhereListener {

    private static final String TAG_WHERE = "WHERE FRAG";
    private static final String TAG_DEP = "DEPARTURE FRAG";
    private static final String TAG_NAV = "NAV FRAG";
    private static final String TAG = "MAIN";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0; //this is not used, I believe
    private boolean mLocationPermission = false; //I request permission even though it's not implemented in this version

    private GoogleMap map; //this isn't used

    MetroService mMetroService;
    MetroRepo mMetroRepo;

    String mRouteNumber; //hold searched route
    String mDirectionValue; //hold searched direction

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationBar navigationBar = NavigationBar.newInstance();
        WhereIsMyBusFragment whereFragment = WhereIsMyBusFragment.newInstance();


        mMetroRepo = new MetroRepo();
        mMetroService = mMetroRepo.mMetroService;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, whereFragment, TAG_WHERE);
        ft.add(R.id.navigation_bar_container, navigationBar, TAG_NAV);
        ft.commit();

        requestLocationPermission(); //this is unnecessary so far
    }

    private void requestLocationPermission() { //straight from your Bees app
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "Have location permission");
            mLocationPermission = true;
        } else {
            Log.d(TAG, "About to request permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){ //straight from your bees app

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mLocationPermission = true;
                Log.d(TAG, "Location permission granted");
            } else {
                mLocationPermission = false;
                Toast.makeText(this, "Location permission must be granted for Metro Bus Tracker to work. Please enable in settings.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void openFindBusFrag() { //when clicking question mark button on nav bar
        WhereIsMyBusFragment whereFrag = WhereIsMyBusFragment.newInstance();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, whereFrag, TAG_WHERE); //set fragment to where bus fragment
    }

    @Override
    public void openDepartureFrag(){ //same as above except with departures fragment
        DeparturesFragment departuresFragment = DeparturesFragment.newInstance();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, departuresFragment, TAG_DEP);
        ft.commit();
    }

    @Override
    public void findRoutesWhere(){ //since both fragments request route data, I use the same method and just differentiate with a string variable
        String fragment = "where";
        requestRoutes(fragment);
    }

    @Override
    public void findRoutesDep(){ //see above
        String fragment = "departures";
        requestRoutes(fragment);
    }

    private void requestRoutes(final String fragment) { //calls api to find array of routes
        mMetroService.getRoutes().enqueue(new Callback<Routes[]>() {
            @Override
            public void onResponse(Call<Routes[]> call, Response<Routes[]> response) {
                Routes[] routes = response.body(); //type data
                sendRoutesToFragment(routes, fragment); //method in main
            }

            @Override
            public void onFailure(Call<Routes[]> call, Throwable t) {
                Log.d(TAG, "Error in retrieving routes: " + t);
                Toast.makeText(getApplicationContext(), "There was an error in retrieving routes", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendRoutesToFragment(Routes[] routes, String fragment) {
        FragmentManager fm = getSupportFragmentManager();
        switch(fragment){
            case "where": //if request came from the where fragment...
                WhereIsMyBusFragment whereFragment = (WhereIsMyBusFragment) fm.findFragmentByTag(TAG_WHERE);
                if(routes != null){
                    whereFragment.setSpinnerItems(routes); //add items to spinner
                }
                break;
            case "departures": //same as above except with departure fragment...
                DeparturesFragment departuresFragmentFragment = (DeparturesFragment) fm.findFragmentByTag(TAG_DEP);
                if(routes != null){
                    departuresFragmentFragment.setRouteSpinnerItems(routes);
                }
                break;
        }
    }

    @Override
    public void findVehicleLocation(Routes route){ //uses coordinates to show bus location on map
        String routeNumber = route.getRoute();
        mMetroService.getVehicleLocation(routeNumber).enqueue(new Callback<VehicleLocations[]>() {
            @Override
            public void onResponse(Call<VehicleLocations[]> call, Response<VehicleLocations[]> response) {
                VehicleLocations[] location = response.body();
                if (location.length > 0) {
                    String uriString = "geo:" + location[0].getVehicleLatitude() + "," + location[0].getVehicleLongitude(); //create coordinates for uri
                    Uri gmmIntentUri = Uri.parse(uriString);//convert string to uri
                    Log.d(TAG, "uriString = " + uriString);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri); //create intent
                    startActivity(mapIntent); //show location on map (doesn't use marker for some reason??)
                } else {
                    Toast.makeText(getApplicationContext(), "That line is currently not running.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<VehicleLocations[]> call, Throwable t) {
                Log.d(TAG, "Vehicle search failed because: " + t);
                Toast.makeText(getApplicationContext(), "There was an error in retrieving vehicle location", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void findDirections(Routes route){ //uses api to find direction bus is heading
        mRouteNumber = route.getRoute();
        mMetroService.getDirection(mRouteNumber).enqueue(new Callback<Directions[]>() {
            @Override
            public void onResponse(Call<Directions[]> call, Response<Directions[]> response) {
                Directions[] directions = response.body();
                if (directions != null) {
                    FragmentManager fm = getSupportFragmentManager();
                    DeparturesFragment df = (DeparturesFragment) fm.findFragmentByTag(TAG_DEP);
                    df.setDirectionSpinner(directions); //set result to spinner
                } else {
                    Toast.makeText(getApplicationContext(), "That line is currently not running.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Directions[]> call, Throwable t) {
                Log.d(TAG, "Direction search failed because: " + t);
                Toast.makeText(getApplicationContext(), "There was an error in retrieving vehicle direction", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void findStops(Directions direction){ //same as above except with stop data
        mDirectionValue = direction.getValue();
        mMetroService.getStops(mRouteNumber, mDirectionValue).enqueue(new Callback<Stops[]>() {
            @Override
            public void onResponse(Call<Stops[]> call, Response<Stops[]> response) {
                Stops[] stops = response.body();
                if (stops != null) {
                    FragmentManager fm = getSupportFragmentManager();
                    DeparturesFragment df = (DeparturesFragment) fm.findFragmentByTag(TAG_DEP);
                    df.setStopSpinner(stops);
                } else {
                    Toast.makeText(getApplicationContext(), "That line is currently not running.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Stops[]> call, Throwable t) {
                Log.d(TAG, "Direction search failed because: " + t);
                Toast.makeText(getApplicationContext(), "There was an error in retrieving vehicle direction", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void findDepartures(Stops stop){ //same as above except it sets departure data points to textViews
        String stopValue = stop.getValue();
        mMetroService.getDepartures(mRouteNumber, mDirectionValue, stopValue).enqueue(new Callback<TimepointDepartures[]>() {
            @Override
            public void onResponse(Call<TimepointDepartures[]> call, Response<TimepointDepartures[]> response) {
                TimepointDepartures[] departures = response.body();
                if(departures != null){
                    FragmentManager fm = getSupportFragmentManager();
                    DeparturesFragment df = (DeparturesFragment) fm.findFragmentByTag(TAG_DEP);
                    df.setDepartures(departures); //set to textviews
                } else {
                    Toast.makeText(getApplicationContext(), "That line is currently not running.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TimepointDepartures[]> call, Throwable t) {
                Log.d(TAG, "Departure search failed because: " + t);
                Toast.makeText(getApplicationContext(), "There was an error in retrieving departure time", Toast.LENGTH_LONG).show();
            }
        });
    }
}
