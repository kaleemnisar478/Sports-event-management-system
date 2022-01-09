package com.sports.sportseventmanagementsystem.helperClasses;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sports.sportseventmanagementsystem.R;

import java.util.List;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {

    private Context context;
    private List<String> electionList;
    private List<List<Tournament>> sectionList;
    private TournementAdapter itemAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView itemRecyclerView;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.section_item_text_view);
            itemRecyclerView = view.findViewById(R.id.item_recycler_view);

        }
    }


    public ScheduleAdapter(Context context, List<String> List, List<List<Tournament>> sectionList) {
        this.context = context;
        this.electionList = List;
        this.sectionList=sectionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_section_scheduled, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String election = electionList.get(position);

        holder.title.setText(election);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.itemRecyclerView.setLayoutManager(linearLayoutManager);
        itemAdapter = new TournementAdapter(context,sectionList.get(position));
        holder.itemRecyclerView.setAdapter(itemAdapter);
    }

    @Override
    public int getItemCount() {
        return electionList.size();
    }


}
