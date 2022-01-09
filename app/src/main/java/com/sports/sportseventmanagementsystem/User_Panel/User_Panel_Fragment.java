package com.sports.sportseventmanagementsystem.User_Panel;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.sports.sportseventmanagementsystem.InProgress.InProgress;
import com.sports.sportseventmanagementsystem.Login;
import com.sports.sportseventmanagementsystem.R;
import com.sports.sportseventmanagementsystem.Results.Results;
import com.sports.sportseventmanagementsystem.Schedule.Scheduled;

public class User_Panel_Fragment extends Fragment {


    private MaterialCardView inProgress,results,settings,schedule;
    private Bundle bundle;

    public User_Panel_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_panel, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Home");

        inProgress=view.findViewById(R.id.inProgress);

        schedule=view.findViewById(R.id.schedule);

        results=view.findViewById(R.id.results);
        settings=view.findViewById(R.id.settings);

        String user=getArguments().getString("user");
        inProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InProgress()).addToBackStack("In_Progress").commit();
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Scheduled()).addToBackStack("schedule").commit();
            }
        });


        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Results()).addToBackStack("results").commit();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Settings()).addToBackStack("Settings").commit();

                Intent intent=new Intent(getActivity(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });


        return view;

    }

}
