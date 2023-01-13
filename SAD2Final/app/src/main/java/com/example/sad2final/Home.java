package com.example.sad2final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements View.OnClickListener {


    private Button userProfile, userLocation, agencies ;
    private ImageButton alertButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        alertButton = (ImageButton) findViewById(R.id.imageButton);
        alertButton.setOnClickListener(this);

        userProfile = (Button) findViewById(R.id.button2);
        userProfile.setOnClickListener(this);

        userLocation = (Button) findViewById(R.id.btn_userloc);
        userLocation.setOnClickListener(this);

        agencies = (Button) findViewById(R.id.btn_agencies);
        agencies.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                startActivity(new Intent(this, InfoAgencies.class));
                break;


        }
    }
}




