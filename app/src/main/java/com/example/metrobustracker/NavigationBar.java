package com.example.metrobustracker;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationBar extends Fragment implements View.OnClickListener {


    NavigationListener mNavigationListener;
    ImageButton whereIsBusButton;
    ImageButton homeButton;
    ImageButton lineMapsButton;

    private static final String TAG = "SEARCH BAR FRAG";

    public interface NavigationListener {
        void openFindBusFrag();
        void openDepartureFrag();
    }



    public NavigationBar() {
        // Required empty public constructor
    }

    public static NavigationBar newInstance(){
        NavigationBar navigationBar = new NavigationBar();
        return navigationBar;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        Log.d(TAG, "onAttach");

        if(context instanceof NavigationListener){
            mNavigationListener = (NavigationListener) context; //attach listener
        } else {
            throw new RuntimeException(context.toString() + " must implement SearchListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_bar, container, false);

        whereIsBusButton = view.findViewById(R.id.where_imageButton);
        whereIsBusButton.setOnClickListener(this);
        homeButton = view.findViewById(R.id.home_imageButton);
        homeButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v){
        int button = v.getId();
        switch (button){
            case R.id.where_imageButton:
                mNavigationListener.openFindBusFrag();
                break;
            case R.id.home_imageButton:
                mNavigationListener.openDepartureFrag();
                break;

        }
    }

}
