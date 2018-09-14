package com.project.itai.FindAPlace.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.project.itai.FindAPlace.R;
import com.project.itai.FindAPlace.beans.Place;
import com.project.itai.FindAPlace.dao.PlacesDao;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoritesFrag.IUserFavAction} interface
 * to handle interaction events.
 */
public class FavoritesFrag extends Fragment {

    private IUserFavAction parentActivity;
    protected PlacesDao placesDao = new PlacesDao((Activity) getContext());
    private List<Place> placesData;
    private ArrayAdapter<Place> adapter;
    private ListView listView;

    public FavoritesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_favorites, container, false);

        Button eraseAll = (Button) fragmentView.findViewById(R.id.deleteAll);
        eraseAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.deleteAllFavorites();
            }
        });

        placesData = new ArrayList<>();
        placesData = placesDao.getAllPlaces();
        adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, placesData);
        listView = fragmentView.findViewById(R.id.listPlaces);
        listView.setAdapter(adapter);

        placesData.add(new Place("Nassau",25.05,-77.4833,"Tmp","tmp"));

        adapter.notifyDataSetChanged();


        return fragmentView;
    }

        @Override
        public void onAttach (Context context){
            super.onAttach(context);
           /* if (context instanceof IUserFavAction) {
                this.parentActivity = (IUserFavAction) context;

            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }*/
        }

        @Override
        public void onDetach () {
            super.onDetach();
            parentActivity = null;
        }

        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */
        public interface IUserFavAction {
            // TODO: Update argument type and name
            void deleteAllFavorites();
        }
    }
