package com.example.metrobustracker;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.metrobustracker.Model.Routes;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


/**
 * A simple {@link Fragment} subclass.
 */
public class WhereIsMyBusFragment extends Fragment {

    private static final String TAG = "WHERE FRAG";

    Spinner routeSpinner;
    TextView titleLabel;
    TextView selectLabel;
    TextView countdownLabel;
    Button findRouteButton;

    final List<Routes> mResponseHolder = new ArrayList<>();

    public WhereIsMyBusFragment() {
        // Required empty public constructor
    }

    public interface WhereListener{
        void findRoutesWhere();
        void findVehicleLocation(Routes route);
    }

    WhereListener mWhereListener;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        Log.d(TAG, "onAttach");

        if(context instanceof WhereIsMyBusFragment.WhereListener){
            mWhereListener = (WhereIsMyBusFragment.WhereListener) context; //attach listener
        } else {
            throw new RuntimeException(context.toString() + " must implement SearchListener");
        }
    }



    public static WhereIsMyBusFragment newInstance(){
        WhereIsMyBusFragment whereIsMyBusFragment = new WhereIsMyBusFragment();
        return whereIsMyBusFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_where_is_my_bus, container, false);
        
        routeSpinner = view.findViewById(R.id.route_spinner);
        mWhereListener.findRoutesWhere();
        titleLabel = view.findViewById(R.id.where_bus_textview);
        selectLabel = view.findViewById(R.id.select_prompt_textView);
        countdownLabel = view.findViewById(R.id.countdown_message_textView);
        countdownLabel.setVisibility(View.INVISIBLE);
        findRouteButton = view.findViewById(R.id.find_route_button);
        findRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findRouteOnMap();
                delayButton();
            }
        });

        return view;
    }

    private void delayButton() { //delays search for bus/train coordinates for 30 seconds, displays user-friendly message
                                    //I will implement this in the departure fragment in the future
        findRouteButton.setEnabled(false); //disables button
        countdownLabel.setVisibility(View.VISIBLE); //countdown appears
        final int delay = 30000;
        final String delayMessage = "To be polite to Metro\'s servers, search is temporarily disabled for: ";
        new CountDownTimer(delay, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                String countdown = delayMessage + millisUntilFinished/1000 + " more seconds."; //displays seconds remaining before you can search again
                countdownLabel.setText(countdown);
            }

            @Override
            public void onFinish() {
                countdownLabel.setVisibility(View.INVISIBLE);
                findRouteButton.setEnabled(true); //can use again after timer completes
            }
        }.start();

    }

    public void setSpinnerItems(Routes[] routesResponse) { //sets route response data to spinner
        List<String> routeNameArray = new ArrayList<>();
        for(Routes route : routesResponse){
            routeNameArray.add(route.getDescription());
            mResponseHolder.add(route);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, routeNameArray);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        routeSpinner.setAdapter(adapter);
    }


    private void findRouteOnMap() { //sends to main to interact with API
        Routes routeToSearch = getCurrentRoute();
        if(routeToSearch != null){
            mWhereListener.findVehicleLocation(routeToSearch);
        }
    }

    private Routes getCurrentRoute() { //retrieve selected route
        String selectedRoute = routeSpinner.getSelectedItem().toString();
        Routes routeToSearch = null;
        for(Routes route : mResponseHolder){
            if(route.getDescription().contains(selectedRoute)){
                routeToSearch = route;
                break;
            }
        }
        return routeToSearch;
    }

}
