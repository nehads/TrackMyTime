package com.javac.dell.trackmytime.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.javac.dell.trackmytime.R;
import com.javac.dell.trackmytime.model.TimeRecords;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<TimeRecords> timeRecordsList;

    class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView date, checkInTimeTv, checkOutTimeTv;

        MyViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            checkInTimeTv = itemView.findViewById(R.id.check_in_time);
            checkOutTimeTv = itemView.findViewById(R.id.check_out_time);

        }
    }

    public RecyclerAdapter(ArrayList<TimeRecords> timeRecordsList) {
        this.timeRecordsList = timeRecordsList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timing, parent, false);

        return new RecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter.MyViewHolder holder, int position) {
        final TimeRecords timeRecordObj = timeRecordsList.get(position);
        holder.date.setText(timeRecordObj.getDates());
        holder.checkInTimeTv.setText(timeRecordObj.getCheckInTime());
        holder.checkOutTimeTv.setText(timeRecordObj.getCheckOutTime());

    }

    @Override
    public int getItemCount() {
        return timeRecordsList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}
