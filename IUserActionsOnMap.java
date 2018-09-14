package com.project.itai.FindAPlace.fragments;

import com.google.android.gms.maps.model.LatLng;

public interface IUserActionsOnMap {
    public void onFocusOnLocation(LatLng location,String name);
}
