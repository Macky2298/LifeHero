package com.example.sad2final.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sad2final.R;
import com.example.sad2final.components.FirebaseData;
import com.example.sad2final.model.User;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private Button register;
    private EditText editTextFullName, editTextEmail, editTextContact, editTextPassword, editTextAddress;
    private ProgressBar progressBar;

    private FirebaseData mFirebaseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseData = new FirebaseData();

        register = (Button) findViewById(R.id.btn_reg);
        register.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextContact = (EditText) findViewById(R.id.contact);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextAddress = (EditText) findViewById(R.id.address);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reg:
                register();
        }
    }

    private void register() {
        String name = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if (name.isEmpty()){
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }

        if (email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email.");
            editTextEmail.requestFocus();
            return;
        }

        if (contact.isEmpty()){
            editTextContact.setError("Your contact number is required!");
            editTextContact.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 8){
            editTextPassword.setError("Min password length should be 8 characters");
            editTextPassword.requestFocus();
            return;
        }

        if (address.isEmpty()){
            editTextAddress.setError("Please input your address");
            editTextAddress.requestFocus();
            return;
        }

        mFirebaseData.getAuthentication().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    User user = new User (name, email, contact, password, address);
                    mFirebaseData.getDatabase().getReference(mFirebaseData.getEndpointUsers())
                        .child(mFirebaseData.getCurrentUser().getUid())
                        .setValue(user).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                Toast.makeText(Signup.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Signup.this, "Failed to register, Try again.",Toast.LENGTH_LONG).show();
                            }
                        });
                }
            });
    }
}