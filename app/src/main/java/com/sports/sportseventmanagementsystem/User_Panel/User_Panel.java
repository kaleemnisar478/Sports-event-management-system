package com.sports.sportseventmanagementsystem.User_Panel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sports.sportseventmanagementsystem.R;

public class User_Panel extends AppCompatActivity {
   private String user;
   private Bundle bundle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        user=getIntent().getStringExtra("user");

        if(savedInstanceState==null) {   //this if will check on rotation as on rotaion item destroy and recreate but due to this save instance selected item remain same
            User_Panel_Fragment voter_panel__fragment = new User_Panel_Fragment();
            bundle=new Bundle();
            bundle.putString("user",user);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, voter_panel__fragment).commit();
            voter_panel__fragment.setArguments(bundle);
        }
    }
}