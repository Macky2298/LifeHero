package com.example.sad2final.view.agency;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sad2final.R;
import com.example.sad2final.components.FirebaseData;
import com.example.sad2final.databinding.FragmentActivityAgencyBinding;
import com.example.sad2final.model.AgencyModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AgencyFragmentActivity extends FragmentActivity implements View.OnClickListener {

    private FragmentActivityAgencyBinding mBinding;
    private FirebaseData mFirebaseData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = FragmentActivityAgencyBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.addAgencyButton.setOnClickListener(this);
        mBinding.btnSave.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);

        RecyclerView recyclerView = mBinding.listItem;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<AgencyModel> agencyArrayList = new ArrayList<>();
        AgencyAdapter agencyAdapter = new AgencyAdapter(this, agencyArrayList);

        recyclerView.setAdapter(agencyAdapter);

        mFirebaseData = new FirebaseData();
        mFirebaseData.getDatabase().getReference(mFirebaseData.getEndpointAgency())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        agencyArrayList.clear();

                        for (DataSnapshot data : snapshot.getChildren()) {
                            AgencyModel agencyModel = data.getValue(AgencyModel.class);
                            agencyArrayList.add(agencyModel);
                        }

                        Collections.reverse(agencyArrayList);
                        agencyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addAgencyButton:
                enableAddingAgency();
                break;
            case R.id.btn_save:
                submitAgency();
                break;
            case R.id.btn_cancel:
                disableAddingAgency();
                break;
        }
    }

    public void submitAgency() {

        String strDepartment = mBinding.departmentEditText.getText().toString();
        String strAddress = mBinding.addressEditText.getText().toString();
        String strContact = mBinding.contactNoEditText.getText().toString();

        if (strDepartment.isEmpty() || strAddress.isEmpty() || strContact.isEmpty()) {
            Toast.makeText(AgencyFragmentActivity.this, "Please provide all information", Toast.LENGTH_LONG).show();
            return;
        }

        AgencyModel agencyData = new AgencyModel(strDepartment, strAddress, strContact);

        mFirebaseData.getDatabase().getReference(mFirebaseData.getEndpointAgency())
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long childCount = 0;
                    if (snapshot.exists()) {
                        childCount = snapshot.getChildrenCount();
                    }

                    mFirebaseData.getDatabase().getReference(mFirebaseData.getEndpointAgency() + "/" + childCount)
                        .setValue(agencyData)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                disableAddingAgency();
                                Toast.makeText(AgencyFragmentActivity.this, "Agency successfully added", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AgencyFragmentActivity.this, "Failed to add agency", Toast.LENGTH_LONG).show();
                            }
                        });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AgencyFragmentActivity.this, "Failed to add agency", Toast.LENGTH_LONG).show();
                }
            });
    }

    public void enableAddingAgency() {
        mBinding.addAgencyLayout.setVisibility(View.VISIBLE);
        mBinding.addAgencyButton.setVisibility(View.GONE);
    }

    public void disableAddingAgency() {
        mBinding.addAgencyLayout.setVisibility(View.GONE);
        mBinding.addAgencyButton.setVisibility(View.VISIBLE);

        mBinding.departmentEditText.setText(null);
        mBinding.addressEditText.setText(null);
        mBinding.contactNoEditText.setText(null);
    }

}