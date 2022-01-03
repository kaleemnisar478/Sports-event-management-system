package com.sports.sportseventmanagementsystem.Admin_Panel.CreateTournament;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sports.sportseventmanagementsystem.R;
import com.sports.sportseventmanagementsystem.helperClasses.Game;
import com.sports.sportseventmanagementsystem.helperClasses.Team;
import com.sports.sportseventmanagementsystem.helperClasses.Tournament;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Tournament_Details extends Fragment {

    private TextInputLayout title,startDate,endDate,startTime,endTime,details;
    private Spinner sportsName;
    private List<String> sports;
    private List<Game> games;
    private List<String> teams;

    private ArrayAdapter<String> sports_adapter;

    private List<KeyPairBoolData> items;


    private Button addDetails;

    ImageView image;

    private Tournament detail;

    int mHour,mMinute;

    int index;
    private DatabaseReference databaseReference;

    private MultiSpinnerSearch multiSelectSpinnerWithSearch;

    public Tournament_Details() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_tournement, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Tournament");

        index=0;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        title=view.findViewById(R.id.title);
        sportsName=view.findViewById(R.id.sports_name);
        image=view.findViewById(R.id.image);
        startDate=view.findViewById(R.id.startDate);
        endDate=view.findViewById(R.id.endDate);
        endTime=view.findViewById(R.id.endTime);
        startTime=view.findViewById(R.id.startTime);
        details=view.findViewById(R.id.Details);
        addDetails=view.findViewById(R.id.add_Details);

        sports= new ArrayList<>();
        games= new ArrayList<>();
        teams= new ArrayList<>();
        items=new ArrayList<>();
        sports_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,sports);
        sports_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsName.setAdapter(sports_adapter);


        multiSelectSpinnerWithSearch = view.findViewById(R.id.multipleItemSelectionSpinner);
        multiSelectSpinnerWithSearch.setSearchEnabled(true);

        // A text that will display in search hint.
        multiSelectSpinnerWithSearch.setSearchHint("Select your mood");

        // Set text that will display when search result not found...
        multiSelectSpinnerWithSearch.setEmptyTitle("Not Data Found!");

        // If you will set the limit, this button will not display automatically.
        multiSelectSpinnerWithSearch.setShowSelectAllButton(true);

        //A text that will display in clear text button
        multiSelectSpinnerWithSearch.setClearText("Close & Clear");


        DatabaseReference databaseReferenceGames= FirebaseDatabase.getInstance().getReference("Games");

        databaseReferenceGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sports.clear();
                games.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Game d = postSnapshot.getValue(Game.class);
                    games.add(d);
                    sports.add(d.getName());
                }
                sports_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sportsName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("asss"+i);
                Game g=games.get(i);
                teamsBox(g.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //title.getEditText().setText(ID);

        databaseReference= FirebaseDatabase.getInstance().getReference("Tournament");

        title.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && title.getEditText().getText().toString().isEmpty())
                {
                    title.setError("Field cannot be empty");
                }
            }

        });

        details.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && details.getEditText().getText().toString().isEmpty())
                {
                    details.setError("Field cannot be empty");
                }
            }

        });


        title.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

                if(s.toString().trim().length()==0) {
                    title.setError("Field cannot be empty");
                }
                else if (!(s.toString().matches(name_checker)))
                {
                    title.setError("Invalid Name. Special Characters and numbers are not acceptable");
                }
                else if(s.length()>0 && s.length()<30) {
                    title.setErrorEnabled(false);
                }
                else if (s.toString().trim().length()>=30){
                    title.setError("Maximum length reached");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        details.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0) {
                    details.setError("Field cannot be empty");
                }
                else if(s.length()>0) {
                    details.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT START DATE");
        final MaterialDatePicker mdp = builder.build();
        startDate.getEditText().setKeyListener(null);
        mdp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                startDate.getEditText().setText(mdp.getHeaderText());

                String start = mdp.getHeaderText()+" "+"08:21 AM";//+"Jan 10, 2021 11:27 PM";
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
                DateFormat format = new SimpleDateFormat("d MMM yyyy hh:mm a", Locale.ENGLISH);
                try {
                    Date startDateTime = format.parse(start);
                    startDate.getEditText().setText(targetFormat.format(startDateTime));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        startDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdp.show(getActivity().getSupportFragmentManager(),"START_DATE_PICKER");
            }
        });

        MaterialDatePicker.Builder builder1 = MaterialDatePicker.Builder.datePicker();
        builder1.setTitleText("SELECT END DATE");
        final MaterialDatePicker mdp1 = builder.build();
        endDate.getEditText().setKeyListener(null);
        mdp1.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                endDate.getEditText().setText(mdp1.getHeaderText());

                String start = mdp1.getHeaderText()+" "+"08:21 AM";//+"Jan 10, 2021 11:27 PM";
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
                DateFormat format = new SimpleDateFormat("d MMM yyyy hh:mm a", Locale.ENGLISH);
                try {
                    Date startDateTime = format.parse(start);
                    endDate.getEditText().setText(targetFormat.format(startDateTime));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        endDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdp1.show(getActivity().getSupportFragmentManager(),"END_Date_PICKER");
            }
        });

        startTime.getEditText().setKeyListener(null);
        startTime.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                mHour=c.get(Calendar.HOUR_OF_DAY);
                mMinute=c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                       String day="AM";
                       String hours=String.valueOf(hourOfDay);
                       String minutes=String.valueOf(minute);
                       if(hourOfDay>12)
                       {
                           hours=String.valueOf((hourOfDay-12));
                           hourOfDay=(hourOfDay-12);
                           day="PM";
                       }
                       if(hourOfDay<10)
                           hours="0"+hourOfDay;

                       if(minute<10)
                            minutes="0"+minute;

                        startTime.getEditText().setText(""+hours+":"+minutes+" "+day);

                    }
                },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });

        endTime.getEditText().setKeyListener(null);
        endTime.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                mHour=c.get(Calendar.HOUR_OF_DAY);
                mMinute=c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String day="AM";
                        String hours=String.valueOf(hourOfDay);
                        String minutes=String.valueOf(minute);
                        if(hourOfDay>12)
                        {
                            hours=String.valueOf((hourOfDay-12));
                            hourOfDay=(hourOfDay-12);
                            day="PM";
                        }
                        if(hourOfDay<10)
                            hours="0"+hourOfDay;

                        if(minute<10)
                            minutes="0"+minute;

                        endTime.getEditText().setText(""+hours+":"+minutes+" "+day);

                    }
                },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });


        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatetitle() | !validateSportsName() | !validateStartDate() | !validateEndDate() | !validateStartTime() | !validateEndTime() | !validateDetails()) {
                    Toast.makeText(getContext(),"All fields are necessary", Toast.LENGTH_LONG).show();
                    return;
                }
                addDetails();
            }
        });

        return view;

    }


    private boolean validatetitle(){
        String val=title.getEditText().getText().toString().trim();
        final String name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

        if(val.isEmpty()){
            title.setError("Field cannot be empty");
            title.requestFocus();
            return false;
        }
        else if(!val.matches(name_checker))
        {
            title.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validateSportsName(){
        String val=title.getEditText().getText().toString().trim();
        final String name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

        if(val.isEmpty()){
            title.setError("Field cannot be empty");
            title.requestFocus();
            return false;
        }
        else if(!val.matches(name_checker))
        {
            title.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateDetails(){
        String val=details.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            details.setError("Field cannot be empty");
            details.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateStartDate(){
        String val=startDate.getEditText().getText().toString();
        if(val.equals("DD/MM/YYYY")){
            startDate.setError("Select date");
            startDate.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateEndDate(){
        String val=endDate.getEditText().getText().toString();
        if(val.equals("DD/MM/YYYY")){
            endDate.setError("Select date");
            endDate.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateStartTime(){
        String val=startTime.getEditText().getText().toString();
        if(val.equals("00:00 AM")){
            startTime.setError("Select Time");
            startTime.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateEndTime(){
        String val=endTime.getEditText().getText().toString();
        if(val.equals("00:00 AM")){
            endTime.setError("Select Time");
            endTime.requestFocus();
            return false;
        }
        return true;
    }


    void addDetails()
    {

        String titlename = title.getEditText().getText().toString();
        String spName = sportsName.getSelectedItem().toString();
        String startD = startDate.getEditText().getText().toString();
        String endD = endDate.getEditText().getText().toString();
        String startT = startTime.getEditText().getText().toString();
        String endT = endTime.getEditText().getText().toString();
        String description = details.getEditText().getText().toString();
        final String ID = databaseReference.push().getKey();

        detail = new Tournament(ID,titlename,"asm",spName,teams,startD,endD,startT,endT,description);

        databaseReference.child(ID).setValue(detail);

        Toast.makeText(getContext(),"Details added Successfully", Toast.LENGTH_LONG).show();

        getFragmentManager().popBackStackImmediate();


    }


    void teamsBox(String id){


        DatabaseReference databaseReferenceTeams= FirebaseDatabase.getInstance().getReference("Games").child(id).child("Teams");

        databaseReferenceTeams.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                index=0;
                items.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Team d = postSnapshot.getValue(Team.class);
                    KeyPairBoolData h = new KeyPairBoolData();
                    index++;
                    h.setId(index);
                    h.setName(d.getName());
                    h.setSelected(false);
                    items.add(h);
                }
                multiSelectSpinnerWithSearch.setItems(items, new MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(List<KeyPairBoolData> items) {
                        teams.clear();
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).isSelected()) {
                                Log.i("new", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                teams.add(items.get(i).getName());
                            }
                        }
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



}
