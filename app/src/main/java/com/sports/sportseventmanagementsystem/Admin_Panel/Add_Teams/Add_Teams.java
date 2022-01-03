package com.sports.sportseventmanagementsystem.Admin_Panel.Add_Teams;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sports.sportseventmanagementsystem.Admin_Panel.Add_Players.Add_Players;
import com.sports.sportseventmanagementsystem.R;
import com.sports.sportseventmanagementsystem.helperClasses.Team;
import com.sports.sportseventmanagementsystem.helperClasses.RecyclerTouchListener;
import com.sports.sportseventmanagementsystem.helperClasses.TeamAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Teams extends Fragment {

    private TextView noParticipant;
    private RecyclerView recyclerView;
    private List<Team> participantList;
    private TeamAdapter participantAdapter;
    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;
    private String gameName,gameID;

    private AddTeamsDialog dialog=null;
    public Add_Teams() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_teams, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        gameName =getArguments().getString("gameName");
        gameID=getArguments().getString("gameId");
        toolbar.setTitle("Add "+gameName+" Teams");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        noParticipant=view.findViewById(R.id.empty_participant_view);
        recyclerView=view.findViewById(R.id.recyclerview);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        participantList=new ArrayList<>();
        participantAdapter = new TeamAdapter(getContext(), participantList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(participantAdapter);
        toggleEmptyParticipants();



        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddParticipantDialog(null,"add");
            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference("Games").child(gameID).child("Teams");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Team c = postSnapshot.getValue(Team.class);
                    participantList.add(c);
                }
                participantAdapter.notifyDataSetChanged();
                toggleEmptyParticipants();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Team c=participantList.get(position);
                Add_Players add_players=new Add_Players();
                Bundle bundle=new Bundle();
                bundle.putString("gameID",gameID);
                bundle.putString("gameName",gameName);
                bundle.putString("teamID",c.getId());
                bundle.putString("teamName",c.getName());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, add_players).addToBackStack("add_players").commit();
                add_players.setArguments(bundle);

            }

            @Override
            public void onLongClick(View view, int position) {
                Team c=participantList.get(position);
                Add_Players add_players=new Add_Players();
                Bundle bundle=new Bundle();
                bundle.putString("gameID",gameID);
                bundle.putString("gameName",gameName);
                bundle.putString("teamID",c.getId());
                bundle.putString("teamName",c.getName());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, add_players).addToBackStack("add_players").commit();
                add_players.setArguments(bundle);

            }
        }));

        return view;

    }


    private void openAddParticipantDialog(Team c, String state) {
        dialog=new AddTeamsDialog(c,state, gameID);
        dialog.show(getActivity().getSupportFragmentManager(),"Teams dialog");// use getChild... see from a link
        dialog.setCancelable(false);     // dialog should not close on touches outside the dialog (and using this another case by default on back press dialog not closes)
        //  dialog.setCanceledOnTouchOutside(false); not work in fragment
       // dialog.getDialog().getButton(AlertDialog.BUTTON_POSITIVE)
    }


    private void toggleEmptyParticipants() {
        // you can check notesList.size() > 0

        if (participantAdapter.getItemCount() > 0) {
            noParticipant.setVisibility(View.GONE);
        } else {
            noParticipant.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }

}
