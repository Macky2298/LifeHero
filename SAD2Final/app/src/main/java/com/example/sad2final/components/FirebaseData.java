package com.example.sad2final.components;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseData {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    public FirebaseData() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
        }
    }

    public FirebaseAuth getAuthentication() {
        return mAuth;
    }

    public FirebaseDatabase getDatabase() {
        return mDatabase;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public String getEndpointUsers() {
        return "Users";
    }

    public String getEndpointReports() {
        if (getCurrentUser() == null)
            return "";

        return getEndpointUsers() + "/" + getCurrentUser().getUid() + "/reports";
    }

    public String getEndpointAgency() {
        if (getCurrentUser() == null)
            return "";

        return getEndpointUsers() + "/" + getCurrentUser().getUid() + "/agency";
    }

}
