package com.project.itai.FindAPlace.fragments;


// The fragment delegates the click event, to the activity
// Because in this specific excercise, we've decided that the button's behavior context related
// context related = the activity decides (the behavior can change in different activities)

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.project.itai.FindAPlace.beans.Place;
import com.project.itai.FindAPlace.constants.PlacesConstants;
import com.project.itai.FindAPlace.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.itai.FindAPlace.dao.PlacesDao;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Fragment1 extends Fragment {

    private IUserActions parentActivity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ArrayList<Place> placesData;
    private ArrayAdapter<Place> adapter;
    private ListView listView;
    private PlacesDao placesDao = new PlacesDao((Activity)getContext());
    long id = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


         View fragmentView = inflater.inflate(R.layout.fragment_fragment1, container, false);
        Log.e(PlacesConstants.MY_NAME, "is container null:" + (container == null));


      //  preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        preferences =  PreferenceManager.getDefaultSharedPreferences(getContext());
        placesData = new ArrayList<>();


        //TODO - not working at the moment: string might be null and hence nullifying placesDats
       /* Gson gson = new Gson();
        String json = preferences.getString("json", null);
        Type type = new TypeToken<ArrayList<Place>>(){}.getType();
        placesData = gson.fromJson(json, type);
        editor= preferences.edit();
        editor.putString("data", null);*/



        placesData.add(new Place("Nassau",25.05,-77.4833,"Tmp","tmp"));
        placesData.add(new Place("Brussels",50.833,4.33,"Tmp","tmp"));
        placesData.add(new Place("Viena",48.2,16.337,"Tmp","tmp"));
        placesData.add(new Place("Sydney",32.109333, 34.855499,"Tmp","tmp"));
        placesData.add(new Place("London",51.507351, -0.127758,"Tmp","tmp"));

        adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, placesData);
        listView = fragmentView.findViewById(R.id.listPlaces);

        listView.setAdapter(adapter);


        adapter.notifyDataSetChanged();





        //enables click on list, function couples click and location
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place place = (Place) parent.getItemAtPosition(position);
                Toast.makeText(getActivity().getApplicationContext(), place.getName() +
                        " " + place.getLat()+ " " + place.getLng() , Toast.LENGTH_LONG).show();
                LatLng newLocation = new LatLng(place.getLat(),place.getLng());
                parentActivity.onFocusOnLocation(newLocation, place.getName());
            }
        });


       listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               Place place = (Place) parent.getItemAtPosition(position);
               Toast.makeText(getActivity().getApplicationContext(), "Adding to Favs:" + place.getName()
                       , Toast.LENGTH_LONG).show();
               placesDao.addPlace(new Place(place.getName(),place.getLat(),place.getLng(),"",""));

               return true;
           }
           });
 //TODO check out if this is problematic
/*
    if (placesData.size() > 0)
    {
        PutJasonPref();

    }*/

    return fragmentView;
    }

    // global shared prefrences accessed
    public void PutJasonPref() {
        editor = preferences.edit();//goes into edit mode
        Gson gson = new Gson();
        String json1 = gson.toJson(placesData);
        editor.putString("json1", json1);//all data inserted has to go through commit
        editor.commit();
    }

    // global shared preferences accessed, no need to commit since data is retrieved
    public void getJasonPref() {

        Gson gson = new Gson();
        String json = preferences.getString("json", null);
        Type type = new TypeToken<ArrayList<Place>>() {}.getType();
        placesData = gson.fromJson(json, type);


    }


    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable("places",placesData);
    }

    public void onRestoreInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        placesData.clear();
        placesData.addAll((ArrayList <Place>) state.getSerializable("places"));
        adapter.notifyDataSetChanged();
    }



    @Override
    // The context is in fact the activity which hosts the fragment
    // This function is being called after the activity is being created
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof IUserActions) {
            // If the activity which holds the current fragment, obeys to the rules in the
            // "contract", defined in the interface ("IUserActions"), then we save a
            // reference to the external activity, in order to call it, each time the button
            // had been pressed
            this.parentActivity = (IUserActions) context;//retrieves context from MainActivity
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IUserActions");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentActivity = null;
    }

    public interface IUserActions {
        void onFocusOnLocation(LatLng newLocation, String name);
    }
}//end of class
