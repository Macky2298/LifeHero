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

    private UserReportAdapter userReportAdapter;
    private final ArrayList<UserReportModel> userReportArrayList = new ArrayList<>();
    private final ArrayList<UserReportModel> userReportDataArrayList = new ArrayList<>();
    private final ArrayList<Date> mReportDateList = new ArrayList<>();
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    private String mSearch = "";
    private Date dateFrom = null;
    private Date dateTo = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.sad2final.databinding.FragmentActivityUserReportBinding mBinding = FragmentActivityUserReportBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        RecyclerView recyclerView = mBinding.listItem;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button dateFromButton = mBinding.dateFromButton;
        Button dateToButton = mBinding.dateToButton;
        SearchView searchView2 = mBinding.reportSearchView2;

        userReportAdapter = new UserReportAdapter(this, userReportArrayList);

        recyclerView.setAdapter(userReportAdapter);

        FirebaseData mFirebaseData = new FirebaseData();
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
                    try {
                        filterList(mSearch);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.e(getClass().getSimpleName(), "Date from " + dateFrom.toString());
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
                    dateToButton.setText(dateTo.toString());
                    try {
                        filterList(mSearch);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.e(getClass().getSimpleName(), "Date to " + dateTo.toString());
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
                mSearch = s;
                try {
                    filterList(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    private void filterList (String searchText) throws ParseException {
        ArrayList<UserReportModel> filteredArrayList = new ArrayList<>(filteredList(userReportDataArrayList,  searchText));

        if (dateFrom == null && dateTo == null && searchText.isEmpty()) {
            filteredArrayList = userReportDataArrayList;
        }

        userReportAdapter.updateData(filteredArrayList);
    }

    private ArrayList<UserReportModel> filteredList(ArrayList<UserReportModel> userReportArrayList, String searchText) throws ParseException {
        ArrayList<UserReportModel> filteredArrayList = new ArrayList<>();
        for (UserReportModel report : userReportArrayList) {
            Date date = mDateFormat.parse(report.getDate());

            assert date != null;
            if (dateFrom == null) {
                if (report.getDepartment().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredArrayList.add(report);
                }
            } else {
                if (dateTo == null) {
                    if (date.getMonth() >= dateFrom.getMonth() &&
                            date.getDate() >= dateFrom.getDate()) {
                        if (searchText.isEmpty()) {
                            if (report.getDepartment().toLowerCase().contains(searchText.toLowerCase())) {
                                filteredArrayList.add(report);
                            }
                        } else {
                            filteredArrayList.add(report);
                        }
                    }
                } else {
                    if ((date.getMonth() >= dateFrom.getMonth() && date.getDate() >= dateFrom.getDate()) &&
                            (date.getMonth() <= dateTo.getMonth() && date.getDate() <= dateTo.getDate())) {
                        if (searchText.isEmpty()) {
                            filteredArrayList.add(report);
                        } else {
                            if (report.getDepartment().toLowerCase().contains(searchText.toLowerCase())) {
                                filteredArrayList.add(report);
                            }
                        }
                    }
                }
            }
        }

        for (UserReportModel report : filteredArrayList) {
            Log.e(getClass().getSimpleName(), "" + report.getDate() + ", " + report.getDepartment());
        }

        return filteredArrayList;
    }

}
