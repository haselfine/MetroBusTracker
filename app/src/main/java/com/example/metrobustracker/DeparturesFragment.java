package com.example.metrobustracker;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.metrobustracker.Model.Directions;
import com.example.metrobustracker.Model.Routes;
import com.example.metrobustracker.Model.Stops;
import com.example.metrobustracker.Model.TimepointDepartures;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeparturesFragment extends Fragment {

    private static final String TAG = "DEPARTURES";

    Button searchButton;
    TextView departureTextOne;
    TextView departureTextTwo;
    TextView departureTextThree;
    TextView timeTextOne;
    TextView timeTextTwo;
    TextView timeTextThree;
    Spinner routeSpinner;
    Spinner directionSpinner;
    Spinner stopsSpinner;
    Button routeOkButton;
    Button directionOkButton;

    final List<Routes> mRouteResponseHolder = new ArrayList<>(); //cache route response
    final List<Directions> mDirectionResponseHolder = new ArrayList<>(); //cache direction response
    final List<Stops> mStopResponseHolder = new ArrayList<>(); //cache stop response
    TextView[] mDepartureTexts; //hold textviews in array for simpler visibility changing

    public DeparturesFragment() {
        // Required empty public constructor
    }



    public interface DepartureListener {
        void findRoutesDep();
        void findDirections(Routes routeToSearch);
        void findStops(Directions directionToSearch);
        void findDepartures(Stops stopToSearch);
    }

    DepartureListener mDepartureListener;

    public static DeparturesFragment newInstance(){
        DeparturesFragment homeFragment = new DeparturesFragment();
        return homeFragment;
    }

    @Override
    public void onAttach(@NonNull Context context){ //attach listener
        super.onAttach(context);

        Log.d(TAG, "onAttach");

        if(context instanceof DepartureListener){
            mDepartureListener = (DepartureListener) context; //attach listener
        } else {
            throw new RuntimeException(context.toString() + " must implement SearchListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_departures, container, false);



        departureTextOne = view.findViewById(R.id.departure_one);
        departureTextTwo = view.findViewById(R.id.departure_two);
        departureTextThree = view.findViewById(R.id.departure_three);
        timeTextOne = view.findViewById(R.id.time_one);
        timeTextTwo = view.findViewById(R.id.time_two);
        timeTextThree = view.findViewById(R.id.time_three);

        mDepartureTexts = new TextView[]{departureTextOne, departureTextTwo, departureTextThree, timeTextOne, timeTextTwo, timeTextThree};

        for(TextView departure : mDepartureTexts){ //set bottom textviews to invisible until search for times
            departure.setVisibility(View.INVISIBLE);
        }

        searchButton = view.findViewById(R.id.get_departures_button);
        searchButton.setOnClickListener(new View.OnClickListener() { //gets departures
            @Override
            public void onClick(View v) {
                search();
            }
        });

        routeSpinner = view.findViewById(R.id.routes_depFrag_spinner);
        directionSpinner = view.findViewById(R.id.direction_spinner);
        stopsSpinner = view.findViewById(R.id.stops_spinner);

        routeOkButton = view.findViewById(R.id.route_ok_button);
        routeOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //when the user says they have chosen their route...
                if(routeSpinner.getSelectedItem() != null) {
                    String selectedRoute = routeSpinner.getSelectedItem().toString(); //takes string of selected item
                    Routes routeToSearch = null;
                    for (Routes route : mRouteResponseHolder) {
                        if (route.getDescription().contains(selectedRoute)) { //searches for match in list
                            routeToSearch = route; //declare route
                            break;
                        }
                    }
                    mDepartureListener.findDirections(routeToSearch); //send route to search for directions
                }
            }
        });

        directionOkButton = view.findViewById(R.id.direction_ok_button);
        directionOkButton.setOnClickListener(new View.OnClickListener() { //same as above except searches stops using direction selected
            @Override
            public void onClick(View v) {
                if(directionSpinner.getSelectedItem() != null) {
                    String selectedDirection = directionSpinner.getSelectedItem().toString();
                    Directions directionToSearch = null;
                    for (Directions directions : mDirectionResponseHolder) {
                        if (directions.getText().contains(selectedDirection)) {
                            directionToSearch = directions;
                            break;
                        }
                    }
                    mDepartureListener.findStops(directionToSearch);
                }
            }
        });

        mDepartureListener.findRoutesDep(); //request array of routes

        return view;
    }

    private void search() { //find departures (same as buttons above) except uses stop data
        String selectedStop = stopsSpinner.getSelectedItem().toString();
        Stops stopToSearch = null;
        for (Stops stop : mStopResponseHolder) {
            if (stop.getText().contains(selectedStop)) {
                stopToSearch = stop;
                break;
            }
        }
        mDepartureListener.findDepartures(stopToSearch);
    }

    public void setRouteSpinnerItems(Routes[] routesResponse) {
        List<String> routeNameArray = new ArrayList<>();
        for (Routes route : routesResponse) { //takes string description and sets to spinner options
            routeNameArray.add(route.getDescription());
            mRouteResponseHolder.add(route); //hold response in global list
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, routeNameArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        routeSpinner.setAdapter(adapter); //set to spinner

    }

    public void setDirectionSpinner(Directions[] directions){ //same as above except with directions
        List<String> directionsTextArray = new ArrayList<>();
        for(Directions direction : directions){
            directionsTextArray.add(direction.getText());
            mDirectionResponseHolder.add(direction);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, directionsTextArray);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        directionSpinner.setAdapter(adapter);
    }

    public void setStopSpinner(Stops[] stops) { //same as above except with stops
        List<String> stopsTextArray = new ArrayList<>();
        for(Stops stop : stops){
            stopsTextArray.add(stop.getText());
            mStopResponseHolder.add(stop);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, stopsTextArray);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        stopsSpinner.setAdapter(adapter);
    }

    public void setDepartures(TimepointDepartures[] departures) { //sets departure data to textviews
        departureTextOne.setText(departures[0].getDescription());
        departureTextTwo.setText(departures[1].getDescription());
        departureTextThree.setText(departures[2].getDescription());

        timeTextOne.setText(departures[0].getDepartureTime()); //this is unformatted. I can't figure out how to format the time correctly
        timeTextTwo.setText(departures[1].getDepartureTime());
        timeTextThree.setText(departures[2].getDepartureTime());

        for(TextView tv : mDepartureTexts){ //set textviews to visible
            tv.setVisibility(View.VISIBLE);
        }
    }
}
