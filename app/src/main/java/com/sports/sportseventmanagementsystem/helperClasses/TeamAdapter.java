package com.sports.sportseventmanagementsystem.helperClasses;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sports.sportseventmanagementsystem.R;

import java.util.List;


public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {

    private Context context;
    private List<Team> participantList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dot;
        public TextView participantName;
        public TextView partyname;
        public ImageView profilePic;



        public MyViewHolder(View view) {
            super(view);
            dot = view.findViewById(R.id.dot);
            participantName = view.findViewById(R.id.participant_Name);
            partyname = view.findViewById(R.id.party);
            profilePic = view.findViewById(R.id.image);

        }
    }


    public TeamAdapter(Context context, List<Team> List) {
        this.context = context;
        this.participantList = List;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_teams, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Team participant = participantList.get(position);


        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.participantName.setText(" "+participant.getName());

        //holder.part.setText(participant.getName());
        if(participant.getImage()!=null)
        {
            Glide.with(context)
                    .load(participant.getImage())
                    .into(holder.profilePic);
        }

    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }


}
