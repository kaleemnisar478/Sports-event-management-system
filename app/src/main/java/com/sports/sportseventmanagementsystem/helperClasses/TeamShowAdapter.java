package com.sports.sportseventmanagementsystem.helperClasses;


import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sports.sportseventmanagementsystem.R;

import java.util.List;


public class TeamShowAdapter extends RecyclerView.Adapter<TeamShowAdapter.MyViewHolder> {

    private Context context;
    private List<String> participantList;
    private String winner;
    private LinearLayout card;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dot;
        public TextView showWin;
        public TextView participantName;



        public MyViewHolder(View view) {
            super(view);
            dot = view.findViewById(R.id.dot);
            showWin = view.findViewById(R.id.winner);
            participantName = view.findViewById(R.id.participant_Name);
            card=view.findViewById(R.id.card);
        }
    }


    public TeamShowAdapter(Context context, List<String> List, String winner) {
        this.context = context;
        this.participantList = List;
        this.winner=winner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_teams_show, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String participant = participantList.get(position);

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.participantName.setText(" "+participant);

        System.out.println(participant+" "+winner);
        if(participant.equals(winner))
        {
            card.setBackgroundColor(Color.RED);
            holder.showWin.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }


}
