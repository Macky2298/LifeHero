package com.example.sad2final.view.userreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sad2final.R;
import com.example.sad2final.model.UserReportModel;

import java.util.ArrayList;

public class UserReportAdapter extends RecyclerView.Adapter<UserReportAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<UserReportModel> mUserReportList;

    public UserReportAdapter(Context context, ArrayList<UserReportModel> userReportList) {
        mContext = context;
        mUserReportList = userReportList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_user_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserReportModel userReport = mUserReportList.get(position);
        holder.dateTextView.setText(userReport.getDate());
        holder.addressTextView.setText(userReport.getAddress());
        holder.locationTextView.setText(userReport.getLocation());
        holder.departmentTextView.setText(userReport.getDepartment());
    }

    @Override
    public int getItemCount() {
        return mUserReportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        TextView addressTextView;
        TextView locationTextView;
        TextView departmentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.dateTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            departmentTextView = itemView.findViewById(R.id.departmentTextView);
        }
    }
}
