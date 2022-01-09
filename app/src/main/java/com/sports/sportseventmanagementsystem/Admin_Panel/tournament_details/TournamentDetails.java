package com.sports.sportseventmanagementsystem.Admin_Panel.tournament_details;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sports.sportseventmanagementsystem.Admin_Panel.Add_Players.Add_Players;
import com.sports.sportseventmanagementsystem.Admin_Panel.Admin_Fragment;
import com.sports.sportseventmanagementsystem.R;
import com.sports.sportseventmanagementsystem.Schedule.Scheduled;
import com.sports.sportseventmanagementsystem.helperClasses.RecyclerTouchListener;
import com.sports.sportseventmanagementsystem.helperClasses.TeamShowAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TournamentDetails extends Fragment {

    private TextView noParticipant;
    private RecyclerView recyclerView;
    private List<String> participantList;
    private TeamShowAdapter participantAdapter;
    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;
    private String tournamentId,tournamentName;

    public TournamentDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tournament_details, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        tournamentId =getArguments().getString("tournamentId");
        tournamentName=getArguments().getString("tournamentName");
        toolbar.setTitle(tournamentName+" Tournament Teams");

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
        participantAdapter = new TeamShowAdapter(getContext(), participantList,null);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(participantAdapter);
        toggleEmptyParticipants();

        databaseReference= FirebaseDatabase.getInstance().getReference("Tournament").child(tournamentId).child("teams");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String team = postSnapshot.getValue(String.class);
                    System.out.println("Team"+team);
                    participantList.add(team);
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
                String c=participantList.get(position);
                startDialog(c);
            }

            @Override
            public void onLongClick(View view, int position) {
                String c=participantList.get(position);
                startDialog(c);

            }
        }));

        return view;

    }

    private void startDialog(String p) {
        android.app.AlertDialog.Builder myAlertDialog = new android.app.AlertDialog.Builder(getContext());
        myAlertDialog.setTitle("Winner");
        myAlertDialog.setMessage(Html.fromHtml("Are you sure "+p+" is winner"+"<br><font color=#FF0000><b>Note:</b></font><br>Once done it can never be undo."));
        myAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Tournament").child(tournamentId).child("winner");
                databaseReference.setValue(p);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Admin_Fragment()).commit();

            }
        });
        myAlertDialog.setNegativeButton("No",null);
        myAlertDialog.show();
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
