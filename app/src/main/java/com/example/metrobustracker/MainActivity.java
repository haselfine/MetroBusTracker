package com.example.metrobustracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.metrobustracker.Model.Routes;
import com.example.metrobustracker.Model.VehicleLocations;
import com.example.metrobustracker.Repository.MetroRepo;
import com.example.metrobustracker.Service.MetroService;
import com.google.android.gms.maps.GoogleMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationBar.NavigationListener,
        HomeFragment.HomeListener,
        WhereIsMyBusFragment.WhereListener {

    private static final String TAG_WHERE = "WHERE FRAG";
    private static final String TAG_HOME = "HOME FRAG";
    private static final String TAG_LINES = "LINES FRAG";
    private static final String TAG_NAV = "NAV FRAG";
    private static final String TAG = "MAIN";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private boolean mLocationPermission = false;

    private GoogleMap map;

    MetroService mMetroService;
    MetroRepo mMetroRepo;

    Button getEm;

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

        requestLocationPermission();
    }

    private void requestLocationPermission() {
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
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){

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
    public void openFindBusFrag() {

    }

    @Override
    public void saveRoute(Routes route){
        
    }

    @Override
    public void findRoutes(){
        mMetroService.getRoutes().enqueue(new Callback<Routes[]>() {
            @Override
            public void onResponse(Call<Routes[]> call, Response<Routes[]> response) {
                Routes[] routes = response.body();
                FragmentManager fm = getSupportFragmentManager();
                WhereIsMyBusFragment whereFragment = (WhereIsMyBusFragment) fm.findFragmentByTag(TAG_WHERE);
                if(routes != null){
                    whereFragment.setSpinnerItems(routes);
                }
            }

            @Override
            public void onFailure(Call<Routes[]> call, Throwable t) {
                Log.d(TAG, "Error in retrieving routes: " + t);
                Toast.makeText(getApplicationContext(), "There was an error in retrieving routes", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void findVehicleLocation(Routes route){
        String routeNumber = route.getRoute();
        mMetroService.getVehicleLocation(routeNumber).enqueue(new Callback<VehicleLocations[]>() {
            @Override
            public void onResponse(Call<VehicleLocations[]> call, Response<VehicleLocations[]> response) {
                VehicleLocations[] location = response.body();
                if (location.length > 0) {
                    String uriString = "geo:" + location[0].getVehicleLatitude() + "," + location[0].getVehicleLongitude();
                    Uri gmmIntentUri = Uri.parse(uriString);
                    Log.d(TAG, "uriString = " + uriString);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "That line is currently not running.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<VehicleLocations[]> call, Throwable t) {
                Log.d(TAG, "Failed because: " + t);
            }
        });
    }
}
