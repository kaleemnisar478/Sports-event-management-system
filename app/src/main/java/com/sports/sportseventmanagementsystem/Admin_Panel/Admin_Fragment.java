package com.sports.sportseventmanagementsystem.Admin_Panel;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sports.sportseventmanagementsystem.Admin_Panel.Add_Games.Add_Games;
import com.sports.sportseventmanagementsystem.Admin_Panel.Add_Teams.ShowGames;
import com.sports.sportseventmanagementsystem.Admin_Panel.CreateTournament.Tournament_Details;
import com.sports.sportseventmanagementsystem.InProgress.InProgress;
import com.sports.sportseventmanagementsystem.Login;
import com.sports.sportseventmanagementsystem.R;

public class Admin_Fragment extends Fragment {


    private MaterialCardView createElection,inProgress,results,settings,addGames,addTeams;
    private String id;
    private Bundle bundle;
    private DatabaseReference databaseReference;

    public Admin_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_panel, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Admin Panel");



        createElection=view.findViewById(R.id.create_tournament);

        results=view.findViewById(R.id.results);
        addGames=view.findViewById(R.id.add_game);
        inProgress=view.findViewById(R.id.in_progress);
        addTeams=view.findViewById(R.id.add_teams);
        settings=view.findViewById(R.id.settings);

        createElection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference= FirebaseDatabase.getInstance().getReference("Elections");
                id = databaseReference.push().getKey();
                bundle=new Bundle();
                bundle.putString("electionID",id);
                Tournament_Details createElection=new Tournament_Details();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, createElection).addToBackStack("CreateElection").commit();
                createElection.setArguments(bundle);

            }
        });


        inProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InProgress()).addToBackStack("In_Progress").commit();
            }
        });
        addGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Add_Games()).addToBackStack("Add Games").commit();
            }
        });
        addTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowGames()).addToBackStack("Add Games").commit();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Settings()).addToBackStack("Settings").commit();
                Intent intent=new Intent(getActivity(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });


        return view;

    }

}
