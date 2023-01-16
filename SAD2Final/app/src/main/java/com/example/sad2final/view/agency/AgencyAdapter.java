package com.example.sad2final.view.agency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sad2final.R;
import com.example.sad2final.model.AgencyModel;

import java.util.ArrayList;

public class AgencyAdapter extends RecyclerView.Adapter<AgencyAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<AgencyModel> mAgencyList;

    public AgencyAdapter(Context context, ArrayList<AgencyModel> agencyList) {
        mContext = context;
        mAgencyList = agencyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_agency, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AgencyModel agencyModel = mAgencyList.get(position);
        holder.departmentTextView.setText(agencyModel.getDepartment());
        holder.addressTextView.setText(agencyModel.getAddress());
        holder.contactTextView.setText(agencyModel.getContactNo());
    }

    @Override
    public int getItemCount() {
        return mAgencyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView departmentTextView;
        TextView addressTextView;
        TextView contactTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            departmentTextView = itemView.findViewById(R.id.departmentTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);
        }
    }

}
