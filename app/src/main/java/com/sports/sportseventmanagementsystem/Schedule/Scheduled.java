package com.sports.sportseventmanagementsystem.Schedule;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.sports.sportseventmanagementsystem.R;
import com.sports.sportseventmanagementsystem.helperClasses.RecyclerTouchListener;
import com.sports.sportseventmanagementsystem.helperClasses.Tournament;
import com.sports.sportseventmanagementsystem.helperClasses.ScheduleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scheduled extends Fragment {

    private TextView noInProgress;
    private RecyclerView recyclerView;
    private List<Tournament> pendingList;
    private List<List<Tournament>> sectionList;
    private List<String> sect;
    private ScheduleAdapter pendingAdapter;
    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;
    Map<String, List<Tournament>> map;


    public Scheduled() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_scheduled, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Scheduled");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        noInProgress=view.findViewById(R.id.empty_inProgress_view);
        recyclerView=view.findViewById(R.id.recyclerview);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pendingList=new ArrayList<>();
        sect=new ArrayList<>();
        sectionList=new ArrayList<>();
        pendingAdapter = new ScheduleAdapter(getContext(), sect,sectionList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(pendingAdapter);
        toggleEmptyParticipants();



        databaseReference= FirebaseDatabase.getInstance().getReference("Tournament");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendingList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Tournament c = postSnapshot.getValue(Tournament.class);
                    pendingList.add(c);

                }
                map = pendingList.stream().collect(Collectors.groupingBy(element
                        -> element.getStartDate()));
                for (Map.Entry<String, List<Tournament>> e : map.entrySet()) {
                    System.out.println(e.getKey());
                    List<Tournament> t=e.getValue();
                    System.out.println(t.get(0).getEndDate());
                    sect.add(e.getKey());
                    sectionList.add(t);

                }
                pendingAdapter.notifyDataSetChanged();
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
                Tournament c=pendingList.get(position);
//                openAddParticipantDialog(c,"edit");

            }

            @Override
            public void onLongClick(View view, int position) {
                Tournament c=pendingList.get(position);
                //openAddParticipantDialog(c,"edit");

            }
        }));

//        title=view.findViewById(R.id.time);
//
//
//
//        new CountDownTimer(300000, 1000) {
//
//            public void onTick(long milliseconds) {
//
//                long dy = TimeUnit.MILLISECONDS.toDays(milliseconds);
//                final long yr = dy / 365;
//                dy %= 365;
//                final long mn = dy / 30;
//                dy %= 30;
//                final long wk = dy / 7;
//                dy %= 7;
//                final long hr = TimeUnit.MILLISECONDS.toHours(milliseconds)
//                        - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds));
//                final long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
//                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
//                final long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
//                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
//                final long ms = TimeUnit.MILLISECONDS.toMillis(milliseconds)
//                        - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(milliseconds));
//
//
//                title.setText("Years :"+yr+" Months :"+mn+"Weeks"+wk+" Days :"+dy+"Hours"+hr+" Minutes "+min+" Seconds "+sec+" Milliseconds"+ mn);
//            }
//
//            public void onFinish() {
//                title.setText("done!");
//            }
//        }.start();


        return view;

    }

    private void toggleEmptyParticipants() {
        // you can check notesList.size() > 0

        if (pendingAdapter.getItemCount() > 0) {
            noInProgress.setVisibility(View.GONE);
        } else {
            noInProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }

}
