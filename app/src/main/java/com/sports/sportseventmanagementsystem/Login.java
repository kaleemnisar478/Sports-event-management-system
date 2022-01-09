package com.sports.sportseventmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sports.sportseventmanagementsystem.Admin_Panel.Admin_Panel;
import com.sports.sportseventmanagementsystem.User_Panel.User_Panel;

public class Login extends AppCompatActivity {
    Button signup,login;

    TextView login_welcome,signin_text;
    ImageView image;
    TextInputLayout username,password;

    ProgressBar login_progressBar;
    DatabaseReference reference;

    private String type;
    private RadioGroup radioGroup;


    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        radioGroup=findViewById(R.id.radiogroup1);
        signup=findViewById(R.id.signup_button);
        login=findViewById(R.id.login_button);
        login_welcome=findViewById(R.id.login_welcome_text);
        signin_text=findViewById(R.id.signin_continue_text);
        image=findViewById(R.id.login_logo);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        login_progressBar=findViewById(R.id.login_progressBar);
        login_progressBar.setVisibility(View.INVISIBLE);
        String text="New User? <font color=#40E0D0>Sign Up</font>";
        signup.setText(Html.fromHtml(text));
//       //todo
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), User_Panel.class);
//                intent.putExtra("username","kaleem");
//                intent.putExtra("type", "AppUser");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            }
//        });
        username.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && username.getEditText().getText().toString().isEmpty())
                {
                    username.setError("Field cannot be empty");
                }
            }

        });

        password.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && password.getEditText().getText().toString().isEmpty())
                {
                    password.setError("Field cannot be empty");
                }
            }

        });


        username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>0){
                    username.setErrorEnabled(false);
                }
                else
                    username.setError("Field cannot be empty");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    password.setErrorEnabled(false);
                }
                else
                    password.setError("Field cannot be empty");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);

            }
        });
    }

    private boolean validateUsername(){
        String val=username.getEditText().getText().toString();

        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            username.requestFocus();
            return false;
        }
        username.setErrorEnabled(false);
        return true;
    }

    private boolean validatePassword(){
        String val=password.getEditText().getText().toString();

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            password.requestFocus();
            return false;
        }

        password.setErrorEnabled(false);
        return true;

    }


    public void loginUser(View view) {
        if(!validatePassword() | !validateUsername()){
            Toast.makeText(getApplicationContext(),"Fill both Fields",Toast.LENGTH_LONG).show();




//            Intent intent=new Intent(getApplicationContext(), home.class);
//            intent.putExtra("username","Kaleem");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);



            return;
        }

        login_progressBar.setVisibility(View.VISIBLE);
        isUser();

    }

    private void isUser() {
        final String userEnteredUsername=username.getEditText().getText().toString();
        final String userEnteredPassword=password.getEditText().getText().toString();

        int userType=radioGroup.getCheckedRadioButtonId();

        type="";
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();

        if(userType==R.id.user || userType==R.id.teacher)
        {   reference = rootNode.getReference("Users").child("AppUsers");
            type="AppUsers";
        }
        else if(userType==R.id.admin)
        {
            reference = rootNode.getReference("Users").child("Admins");
            type="Admins";
        }
        else
        {
            type="None";

        }

        Query checkUser=reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    login_progressBar.setVisibility(View.INVISIBLE);

                    username.setErrorEnabled(false);

                    String passFromDb=dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    if(passFromDb.equals(userEnteredPassword)){

                        password.setErrorEnabled(false);
                        if(type=="Admins") {
                            Intent intent = new Intent(getApplicationContext(), Admin_Panel.class);
                            intent.putExtra("username", userEnteredUsername);
                            intent.putExtra("type", type);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else if(type=="AppUsers")
                        {
                            Intent intent = new Intent(getApplicationContext(), User_Panel.class);
                            intent.putExtra("username", userEnteredUsername);
                            intent.putExtra("type", type);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                    else {
                        password.setError("Wrong Password...");
                        password.requestFocus();
                    }
                }
                else {

                    login_progressBar.setVisibility(View.INVISIBLE);


                    username.setError("Username does not exist");
                    username.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void forgetPassword(View view) {
        Intent intent=new Intent(getApplicationContext(),ForgetPassword.class);
        startActivity(intent);
    }
}