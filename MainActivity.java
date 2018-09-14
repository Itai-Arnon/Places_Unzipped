package com.project.itai.FindAPlace.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.project.itai.FindAPlace.beans.Place;
import com.project.itai.FindAPlace.controllers.ControllersFactory;
import com.project.itai.FindAPlace.dao.PlacesDao;
import com.project.itai.FindAPlace.fragments.FavoritesFrag;
import com.project.itai.FindAPlace.fragments.Fragment1;
import com.project.itai.FindAPlace.R;
import com.project.itai.FindAPlace.fragments.IUserFavAction;
import com.project.itai.FindAPlace.fragments.IUserFavAction;
import com.project.itai.FindAPlace.receiver.PowerConnectionReceiver;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends FragmentActivity implements Fragment1.IUserActions , IUserFavAction {
    private Fragment1.IUserActions userActionsController;
    private PlacesDao placesDao = new PlacesDao(this);
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private Boolean isTablet=null;


    //TODO based on Eran's thing - look at photo in mobile

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userActionsController = ControllersFactory.createUserInteractionsController(this);

        //==06092018
        // Runtime permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If no permission to location ask permission from the user
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},99 );
            }
            return;
        }

        // Getting a tablet or a mobile controller
        // the factory hides the decision making.
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        this.registerReceiver(new PowerConnectionReceiver(),filter);

        //  ViewPager and a PagerAdapter
        viewPager =  findViewById(R.id.pager); //fragment layout into pager layout
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private Fragment[] fragments;//class var

        public ScreenSlidePagerAdapter(FragmentManager manager) {
            super(manager);
            fragments = new Fragment[2];
            fragments[0] = new Fragment1();
            fragments[1] = new FavoritesFrag();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 2;
        }//pager function
    } // end of inner class




    @Override
    //this function activates the function through whichever controller was created in the factory
    public void onFocusOnLocation(LatLng newLocation , String name) {//name is restricted no google places
     // Carrying out the user's request, using our controller
        userActionsController.onFocusOnLocation(newLocation,name);
    }

    @Override
    public void deleteAllFavorites() {

        placesDao.deleteAllPlaces();
    }

    @Override
    //the activity stops being visible
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

