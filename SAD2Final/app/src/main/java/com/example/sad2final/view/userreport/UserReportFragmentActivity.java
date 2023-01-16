package com.example.sad2final.view.userreport;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sad2final.components.FirebaseData;
import com.example.sad2final.databinding.FragmentActivityUserReportBinding;
import com.example.sad2final.model.UserReportModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class UserReportFragmentActivity extends FragmentActivity {

    private FragmentActivityUserReportBinding mBinding;
    private FirebaseData mFirebaseData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = FragmentActivityUserReportBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        RecyclerView recyclerView = mBinding.listItem;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<UserReportModel> userReportArrayList = new ArrayList<>();
        UserReportAdapter userReportAdapter = new UserReportAdapter(this, userReportArrayList);

        recyclerView.setAdapter(userReportAdapter);

        mFirebaseData = new FirebaseData();
        mFirebaseData.getDatabase().getReference(mFirebaseData.getEndpointReports())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            UserReportModel userReport = data.getValue(UserReportModel.class);
                            userReportArrayList.add(userReport);
                        }

                        Collections.reverse(userReportArrayList);
                        userReportAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
