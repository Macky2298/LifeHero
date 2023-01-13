package com.example.sad2final;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class RescueTeams extends AppCompatActivity implements View.OnClickListener {

    private ImageButton police, fire, hospital, sar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_teams);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);

        police = (ImageButton) findViewById(R.id.rt_policebtn);
        police.setOnClickListener(this);

        fire = (ImageButton) findViewById(R.id.rt_firebtn);
        fire.setOnClickListener(this);

        hospital = (ImageButton) findViewById(R.id.rt_hospitalbtn);
        hospital.setOnClickListener(this);

        sar = (ImageButton) findViewById(R.id.rt_sarbtn);
        sar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        AlertDialog dialog = new AlertDialog.Builder(this)
        .setTitle("Alert Confirmation")
        .setMessage("Are you sure you want to send an alert?")
        .setPositiveButton("YES", null)
        .setNegativeButton("Cancel", null)
        .show();


        switch (v.getId()) {


            case R.id.rt_policebtn:

                String number = "1";
                String msg1 = "ALERT! NEED RESCUE! Current location Latitude: 14.73270, Longitude: 121.01531";
                SmsManager smsManager1 = SmsManager.getDefault();
                smsManager1.sendTextMessage(number, null, msg1, null, null);


                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        Toast.makeText(RescueTeams.this, "Alert Sent", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });



            case R.id.rt_hospitalbtn:

            Button positiveButton2 = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(RescueTeams.this, "Alert Sent", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();


                }
            });

            case R.id.rt_firebtn:

                Button positiveButton3 = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton3.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RescueTeams.this, "Alert Sent", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });


            case R.id.rt_sarbtn:

                Button positiveButton4 = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton4.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RescueTeams.this, "Alert Sent", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });


        }

    }
}