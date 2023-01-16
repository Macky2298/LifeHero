package com.example.sad2final.components;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

public class LocationHandler {

    public interface LocationListener {

        void onLocationSuccess(Location location);

    }

    Activity mActivity;
    FusedLocationProviderClient mClient;

    public LocationHandler(Activity activity) {
        mActivity = activity;
        mClient = LocationServices.getFusedLocationProviderClient(mActivity);
    }

    public void getLocation(LocationListener listener) {
        if (mActivity == null)
            return;

        if (ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        mActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> task = mClient.getLastLocation();
        task.addOnSuccessListener(listener::onLocationSuccess);
    }

}
