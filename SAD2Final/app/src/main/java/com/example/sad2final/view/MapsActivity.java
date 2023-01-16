package com.example.sad2final.view;

import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;

import com.example.sad2final.R;
import com.example.sad2final.components.LocationHandler;
import com.example.sad2final.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    LocationHandler mLocationHandler;
    SupportMapFragment mSupportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mLocationHandler = new LocationHandler(this);
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mLocationHandler.getLocation(location -> {
            if (location != null) {
                setupMap(location);
            }
        });
    }

    private void setupMap(Location location) {
        if (location == null)
            return;

        mSupportMapFragment.getMapAsync(googleMap -> {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions options = new MarkerOptions().position(latLng).title("Me");
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            googleMap.addMarker(options);
        });
    }

}