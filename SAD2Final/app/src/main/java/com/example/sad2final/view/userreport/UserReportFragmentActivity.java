package com.example.sad2final.view.userreport;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserReportFragmentActivity extends FragmentActivity {

    private enum SearchType {
        DATE,
        DEPARTMENT
    }

    private FragmentActivityUserReportBinding mBinding;
    private FirebaseData mFirebaseData;
    private UserReportAdapter userReportAdapter;
    private ArrayList<UserReportModel> userReportArrayList = new ArrayList<>();
    private ArrayList<UserReportModel> userReportDataArrayList = new ArrayList<>();
    private ArrayList<Date> mReportDateList = new ArrayList<>();
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    private Date dateFrom = null;
    private Date dateTo = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = FragmentActivityUserReportBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        RecyclerView recyclerView = mBinding.listItem;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button dateFromButton = mBinding.dateFromButton;
        Button dateToButton = mBinding.dateToButton;
        SearchView searchView2 = mBinding.reportSearchView2;

        userReportAdapter = new UserReportAdapter(this, userReportArrayList);

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
                            try {
                                assert userReport != null;
                                Date date = mDateFormat.parse(userReport.getDate());
                                mReportDateList.add(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        Collections.reverse(userReportArrayList);
                        userReportAdapter.notifyDataSetChanged();

                        for (Date date : mReportDateList) {
                            Log.e(getClass().getSimpleName(), date.toString());
                        }

                        if (userReportDataArrayList.isEmpty()) {
                            userReportDataArrayList.addAll(userReportArrayList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // FROM
        dateFromButton.clearFocus();
        dateFromButton.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int date = calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog = new DatePickerDialog(UserReportFragmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    dateFrom = new Date(year, month, dayOfMonth);
                    dateFromButton.setText(dateFrom.toString());
                    filterByDate(dateFrom, dateTo);
                    Log.e(getClass().getSimpleName(), dateFrom.toString());
                }
            }, year, month, date);
            datePickerDialog.show();
        });

        // TO
        dateToButton.clearFocus();
        dateToButton.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int date = calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog = new DatePickerDialog(UserReportFragmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    dateTo = new Date(year, month, dayOfMonth);
                    dateTo.setText(dateFrom.toString());
                    filterByDate(dateFrom, dateTo);
                    Log.e(getClass().getSimpleName(), dateFrom.toString());
                }
            }, year, month, date);
            datePickerDialog.show();
        });

        // DEPARTMENT
        searchView2.clearFocus();
        searchView2.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(getClass().getSimpleName(), "User search input: " + s);
                filterList(SearchType.DEPARTMENT, s);
                return true;
            }
        });
    }
    
    private void filterByDate(Date fromDate, Date toDate) {
        if (fromDate == null) {
            Log.e(getClass().getSimpleName(), "From Date is empty");
            return;
        }

        List<Date> dateList = new ArrayList<>();
        for (Date date : mReportDateList) {
            if (toDate == null) {
                if (fromDate.after(new Date())) {
                    dateList.add(date);
                }

                continue;
            }

            if (fromDate.after(toDate)) {
                dateList.add(date);
            }
        }

        for (Date date : dateList) {
            Log.e(getClass().getSimpleName(), date.toString());
        }
    }

    private void filterList (SearchType searchType, String searchText) {
        ArrayList<UserReportModel> filteredArrayList = new ArrayList<>(filteredList(userReportDataArrayList, searchType, searchText));

        if (searchText.isEmpty()) {
            filteredArrayList = userReportDataArrayList;
        } else {
            if (!filteredArrayList.isEmpty()) {
                for (UserReportModel reportModel : userReportArrayList) {
                    if (reportModel.getDepartment().toLowerCase().contains(searchText.toLowerCase())) {
                        filteredArrayList.add(reportModel);
                    }
                }
            } else {
                filteredArrayList = userReportDataArrayList;
            }
        }
        userReportAdapter.updateData(filteredArrayList);
    }

    private ArrayList<UserReportModel> filteredList(ArrayList<UserReportModel> userReportArrayList, SearchType searchType, String searchText) {
        ArrayList<UserReportModel> filteredArrayList = new ArrayList<>();
        for (UserReportModel reportModel : userReportArrayList) {
            if (searchType.equals(SearchType.DATE)) {
                if (reportModel.getDate().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredArrayList.add(reportModel);
                }
            } else if (searchType.equals(SearchType.DEPARTMENT)) {
                if (reportModel.getDepartment().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredArrayList.add(reportModel);
                }
            }
        }

        return filteredArrayList;
    }

}
