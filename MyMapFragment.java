package com.project.itai.FindAPlace.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.itai.FindAPlace.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class MyMapFragment extends Fragment implements OnMapReadyCallback, IUserActionsOnMap {

    private GoogleMap mMap;
    private LatLng currentLocation;
    private String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This section will need refactoring on the long run

        LatLng defaultLocation = new LatLng(-33.865143, 151.209900);
        this.currentLocation = defaultLocation;

        // Extracrting the intent from the wrapping activity e.g Second Activity/MapActivity
        Intent intent = ((Activity) getContext()).getIntent();

        // Initializing the map's location based on a command sent by the previous activity
        // This code segment is ONLY relevant to mobile mode, and not tablet mode
        String commandName = intent.getStringExtra("commandName");
        if (commandName != null && commandName.equals("focusOnLocation")) {
            double latitude = intent.getDoubleExtra("latitude", 0);
            double longitude = intent.getDoubleExtra("longitude", 0);
            name = intent.getStringExtra("name");
            this.currentLocation = new LatLng(latitude, longitude);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       //==
        mMap.moveCamera(CameraUpdateFactory.newLatLng(this.currentLocation));
        //==
        mMap.addMarker(new MarkerOptions().position(this.currentLocation).title(name));
    }

    @Override
    public void onFocusOnLocation(LatLng newLocation, String name) {
        this.currentLocation = newLocation;
        this.name = name;
        if (mMap != null) {
            mMap.clear();
            mMap.animateCamera(CameraUpdateFactory.newLatLng(newLocation));
            mMap.addMarker(new MarkerOptions().position(newLocation).title(name));
        }
    }
}
