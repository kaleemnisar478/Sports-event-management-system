package com.sports.sportseventmanagementsystem.User_Panel;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.sports.sportseventmanagementsystem.Login;
import com.sports.sportseventmanagementsystem.R;

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

                bundle=new Bundle();
                bundle.putString("user",user);
//                InProgress_Voter inProgress_voter= new InProgress_Voter();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, inProgress_voter).addToBackStack("In Progress").commit();
//                inProgress_voter.setArguments(bundle);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle=new Bundle();
                bundle.putString("user",user);
//                schedule_Voter schedule_voter= new schedule_Voter();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, schedule_voter).addToBackStack("schedule").commit();
//                schedule_voter.setArguments(bundle);
            }
        });


        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Results_Voter()).addToBackStack("Results").commit();
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
