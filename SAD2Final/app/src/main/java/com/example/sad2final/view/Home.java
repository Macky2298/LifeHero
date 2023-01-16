package com.example.sad2final.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.sad2final.R;
import com.example.sad2final.view.agency.AgencyFragmentActivity;
import com.example.sad2final.view.userreport.UserReportFragmentActivity;

public class Home extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        ImageButton alertButton = (ImageButton) findViewById(R.id.imageButton);
        alertButton.setOnClickListener(this);

        Button userReport = (Button) findViewById(R.id.btn_user_data);
        userReport.setOnClickListener(this);

        Button userProfile = (Button) findViewById(R.id.button2);
        userProfile.setOnClickListener(this);

        Button userLocation = (Button) findViewById(R.id.btn_userloc);
        userLocation.setOnClickListener(this);

        Button agencies = (Button) findViewById(R.id.btn_agencies);
        agencies.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_data:
                startActivity(new Intent(this, UserReportFragmentActivity.class));
                break;

            case R.id.button2:
                startActivity(new Intent(this, profile.class));
                break;

            case R.id.imageButton:
                startActivity(new Intent(this, RescueTeams.class));
                break;

            case R.id.btn_userloc:
                startActivity(new Intent(this, MapsActivity.class));
                break;

            case R.id.btn_agencies:
                startActivity(new Intent(this, AgencyFragmentActivity.class));
                break;
        }
    }
}




