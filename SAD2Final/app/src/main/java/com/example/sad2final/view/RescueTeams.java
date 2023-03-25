package com.example.sad2final.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sad2final.R;
import com.example.sad2final.components.FirebaseData;
import com.example.sad2final.components.LocationHandler;
import com.example.sad2final.model.UserReportModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RescueTeams extends AppCompatActivity implements View.OnClickListener {

    private LocationHandler mLocationHandler;
    private Location mLocation;
    private FirebaseData mFirebaseData;
    private SmsManager mSmsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_teams);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);

        mLocationHandler = new LocationHandler(this);
        mFirebaseData = new FirebaseData();
        mSmsManager = SmsManager.getDefault();

        Button police = (Button) findViewById(R.id.rt_policebtn);
        police.setOnClickListener(this);

        Button fire = (Button) findViewById(R.id.rt_firebtn);
        fire.setOnClickListener(this);

        Button hospital = (Button) findViewById(R.id.rt_hospitalbtn);
        hospital.setOnClickListener(this);

        Button sar = (Button) findViewById(R.id.rt_sarbtn);
        sar.setOnClickListener(this);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        mLocationHandler.getLocation(location -> {
            mLocation = location;

            String alertMessage = "Sorry we cannot locate you! \nNo internet connection";

            String policeNo = "09123456789";
            String hospitalNo = "0912345678*";
            String fireNo = "091234567**";
            String sarNo = "09123456***";

            String policeStr = "Police";
            String hospitalStr = "Hospital";
            String fireStr = "Fire Department";
            String sarStr = "Search and Rescue Team";

            String number = "";
            String departmentStr = "";

            switch (v.getId()) {
                case R.id.rt_policebtn:
                    number = policeNo;
                    departmentStr = policeStr;
                    break;
                case R.id.rt_hospitalbtn:
                    number = hospitalNo;
                    departmentStr = hospitalStr;
                    break;
                case R.id.rt_firebtn:
                    number = fireNo;
                    departmentStr = fireStr;
                    break;
                case R.id.rt_sarbtn:
                    number = sarNo;
                    departmentStr = sarStr;
                    break;
            }

            if (mLocation != null) {
                try {
                    Geocoder geocoder = new Geocoder(RescueTeams.this, Locale.getDefault());
                    List<Address> addressList = geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            1);
                    String address = addressList.get(0).getAddressLine(0);

                    UserReportModel reportData = new UserReportModel(
                            "" + new Date(),
                            "" + address,
                            "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(),
                            departmentStr
                    );

                    alert(number,
                        "ALERT! NEED RESCUE!" +
                        "\n\nCurrent location Latitude: " + mLocation.getLatitude() + ", Longitude: " + mLocation.getLongitude() +
                        "\n\nCurrent address: " + address,
                        reportData);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), e.getMessage(), e);
                }
            } else {
                alert(number, departmentStr + " " + alertMessage, null);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void alert(String number, String alertMessage, UserReportModel reportData) {
        new AlertDialog.Builder(this)
            .setTitle("Alert Confirmation")
            .setMessage("Are you sure you want to send an alert?")
            .setPositiveButton("YES", (dialog1, which) -> {
                if (reportData != null) {
                    sendToRecordAlert(reportData);
                }

                mSmsManager.sendTextMessage(number, null, alertMessage, null, null);
                Toast.makeText(RescueTeams.this, alertMessage, Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void sendToRecordAlert(UserReportModel reportData) {
        mFirebaseData.getDatabase()
            .getReference(mFirebaseData.getEndpointReports())
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long childCount = 0;
                    if (snapshot.exists()) {
                        childCount = snapshot.getChildrenCount();
                    }
                    mFirebaseData.getDatabase()
                            .getReference(mFirebaseData.getEndpointReports() + "/" + childCount)
                            .setValue(reportData)
                            .addOnCompleteListener(task -> {
                                if(!task.isSuccessful()) {
                                    Toast.makeText(RescueTeams.this, "Failed to insert data, Try again.", Toast.LENGTH_LONG).show();
                                }
                            });

                    Log.e(getClass().getSimpleName(), "Success");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(getClass().getSimpleName(), error.getMessage());
                }
            });
    }

}