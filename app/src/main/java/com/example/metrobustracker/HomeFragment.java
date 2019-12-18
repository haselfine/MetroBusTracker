package com.example.metrobustracker;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.metrobustracker.Model.Routes;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HOME";

    Button routesButton;
    TextView routesText;

    public HomeFragment() {
        // Required empty public constructor
    }

    public interface HomeListener {

    }

    HomeListener mHomeListener;

    public static HomeFragment newInstance(){
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        Log.d(TAG, "onAttach");

        if(context instanceof HomeFragment.HomeListener){
            mHomeListener = (HomeFragment.HomeListener) context; //attach listener
        } else {
            throw new RuntimeException(context.toString() + " must implement SearchListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        routesButton = view.findViewById(R.id.get_routes_button);
        routesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        routesText = view.findViewById(R.id.routes_textView);

        return view;
    }

    public void setRoutesText(Routes[] routesResponse){

    }
}
