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
import com.sports.sportseventmanagementsystem.Admin_Panel.Add_Games.AddGameDialog;
import com.sports.sportseventmanagementsystem.R;
import com.sports.sportseventmanagementsystem.helperClasses.Game;
import com.sports.sportseventmanagementsystem.helperClasses.GameAdapter;
import com.sports.sportseventmanagementsystem.helperClasses.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowGames extends Fragment {

    private TextView noParticipant;
    private RecyclerView recyclerView;
    private List<Game> participantList;
    private GameAdapter participantAdapter;
    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;
    private String ID;

    private AddGameDialog dialog=null;
    public ShowGames() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_games_teams, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Games (Select to add)");

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
        participantAdapter = new GameAdapter(getContext(), participantList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(participantAdapter);
        toggleEmptyParticipants();



        databaseReference= FirebaseDatabase.getInstance().getReference("Games");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Game c = postSnapshot.getValue(Game.class);
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
                Game c=participantList.get(position);
                Add_Teams add_teams=new Add_Teams();
                Bundle bundle=new Bundle();
                bundle.putString("gameId",c.getId());
                bundle.putString("gameName",c.getName());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, add_teams).addToBackStack("add_teams").commit();
                add_teams.setArguments(bundle);

            }

            @Override
            public void onLongClick(View view, int position) {
                Game c=participantList.get(position);
                Add_Teams add_teams=new Add_Teams();
                Bundle bundle=new Bundle();
                bundle.putString("gameId",c.getId());
                bundle.putString("gameName",c.getName());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, add_teams).addToBackStack("add_teams").commit();
                add_teams.setArguments(bundle);

            }
        }));

        return view;

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
