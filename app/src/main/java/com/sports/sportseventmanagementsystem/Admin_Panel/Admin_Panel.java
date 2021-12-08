package com.sports.sportseventmanagementsystem.Admin_Panel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sports.sportseventmanagementsystem.R;

public class Admin_Panel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);


        if(savedInstanceState==null) {   //this if will check on rotation as on rotaion item destroy and recreate but due to this save instance selected item remain same
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Admin_Fragment()).commit();
        }

    }
}