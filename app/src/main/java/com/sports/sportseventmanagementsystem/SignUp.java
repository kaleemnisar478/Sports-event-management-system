package com.sports.sportseventmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.sports.sportseventmanagementsystem.helperClasses.User;

public class SignUp extends AppCompatActivity {
    private TextInputLayout fullname,username,phone,email,password,dob;
    private CountryCodePicker ccp;
    private Button login,signup;
    private DatabaseReference reference;
    int count;
    private String name,u_name,mail,selected_country_code,number;
    private String type;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        login=findViewById(R.id.signup_login_button);
        String text="Already have an account? <font color=#40E0D0>Login</font>";
        login.setText(Html.fromHtml(text));
        dob=findViewById(R.id.doj);

        radioGroup=findViewById(R.id.radiogroup);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        final MaterialDatePicker mdp = builder.build();
        dob.getEditText().setKeyListener(null);
        mdp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dob.getEditText().setText(mdp.getHeaderText());
            }
        });
        dob.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdp.show(getSupportFragmentManager(),"DOB_PICKER");
            }
        });


//        Spinner spinner = (Spinner) findViewById(R.id.floors);
//// Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.floors, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//        spinner.setAdapter(adapter);



        fullname=findViewById(R.id.full_name);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        ccp = findViewById(R.id.cpp);
        phone=findViewById(R.id.phone);
        password=findViewById(R.id.password);



        // error message for first time

        fullname.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && fullname.getEditText().getText().toString().isEmpty())
                {
                    fullname.setError("Field cannot be empty");
                }
            }

        });
        username.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && username.getEditText().getText().toString().isEmpty())
                {
                    username.setError("Field cannot be empty");
                }
            }

        });
        email.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && email.getEditText().getText().toString().isEmpty())
                {
                    email.setError("Field cannot be empty");
                }
            }

        });

        phone.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && phone.getEditText().getText().toString().isEmpty())
                {
                    phone.setError("Field cannot be empty");
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






        //after first letter errors
        fullname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String full_name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

                if(s.length()==0) {
                    fullname.setError("Field cannot be empty");
                }
                else if (!(s.toString().matches(full_name_checker)))
                {
                    fullname.setError("Invalid Name. Special Characters and numbers are not acceptable");
                }
                else if(s.length()>0 && s.length()<30) {
                    fullname.setErrorEnabled(false);
                }
                else if (s.length()>=30){
                    fullname.setError("Maximum length reached");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                final String usernamecheck="^[a-zA-Z0-9_@.-]{5,21}$";

                if(s.length()==0) {
                    username.setError("Field cannot be empty");
                }
                else if (s.length()<5){
                    username.setError("Minimum length is 5");
                }
                else if (s.length()>=21){
                    username.setError("Maximum length reached");
                }
                else if (!(s.toString().matches(usernamecheck)))
                {
                    username.setError("Invalid Username");
                }
                else
                {

                    username.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String email_checker="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

                if(s.length()==0) {
                    email.setError("Field cannot be empty");
                }
                else if (!(s.toString().matches(email_checker)))
                {
                    email.setError("Invalid Email");
                }
                else
                {
                    email.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0) {
                    phone.setError("Field cannot be empty");
                }
                else if (s.length()<10){
                    phone.setError("Invalid Phone number");
                }
                else
                {
                    phone.setErrorEnabled(false);
                }

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
                if(s.length()==0) {
                    password.setError("Field cannot be empty");
                }
                else if (s.length()>=21){
                    password.setError("Maximum length reached");
                }
                else if (s.length()<7)
                {
                    password.setError("Minimum length seven");
                }
                else
                {
                    password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private boolean validateFullname(){
        String val=fullname.getEditText().getText().toString();
        final String full_name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

        if(val.isEmpty()){
            fullname.setError("Field cannot be empty");
            fullname.requestFocus();
            return false;
        }
        else if(!val.matches(full_name_checker))
        {
            fullname.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateUsername(){
        String val=username.getEditText().getText().toString();
        final String username_check="^[a-zA-Z0-9_@.-]{5,21}$";

        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            username.requestFocus();
            return false;
        }
        else if(!val.matches(username_check))
        {
            username.requestFocus();
            return false;
        }
        return true;

    }

    private boolean validateEmail(){
        String val=email.getEditText().getText().toString();
        final String emailchecker="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

        if(val.isEmpty()){
            email.setError("Field cannot be empty");
            email.requestFocus();
            return false;
        }
        else if(!val.matches(emailchecker))
        {
            email.requestFocus();
            return false;
        }
        return true;

    }

    private boolean validatePhone(){
        String val=phone.getEditText().getText().toString();
        if(val.isEmpty()){
            phone.setError("Field cannot be empty");
            phone.requestFocus();
            return false;
        }
        else if(val.length()<10)
        {
            phone.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePassword(){
        String val=password.getEditText().getText().toString();

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            password.requestFocus();
            return false;
        }

        else if(val.length()<7)
        {
            password.requestFocus();
            return false;
        }
        return true;

    }


    public void registerUser(View view) {
        if( !validatePassword() | !validatePhone() | !validateEmail()  | !validateUsername() | !validateFullname()){
            Toast.makeText(getApplicationContext(),"Fill all Fields with no errors",Toast.LENGTH_LONG).show();

            return;
        }
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



        name=fullname.getEditText().getText().toString();
        u_name=username.getEditText().getText().toString();
        mail=email.getEditText().getText().toString();
        selected_country_code = ccp.getSelectedCountryCodeWithPlus().toString();
        number=selected_country_code+phone.getEditText().getText().toString();


        count=0;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    if(u.getEmail().equals(u_name)||u.getEmail().equals(mail))
                    {
                        count++;
                        email.setError("Account already exists. If you don't know your password go for forget password");
                        email.setErrorEnabled(true);
                        Toast.makeText(getApplicationContext(),"Account already exists. If you don't know your password go for forget password",Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(u.getUsername().equals(u_name)||u.getUsername().equals(mail))
                    {
                        count++;
                        username.setError("Account already exists. If you don't know your password go for forget password");
                        username.setErrorEnabled(true);
                        Toast.makeText(getApplicationContext(),"Account already exists. If you don't know your password go for forget password",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                if(count==0){
                    if(number.charAt(3)=='0' && number.substring(0, 3).equals("+92"))
                    {
                        String temp=number.substring(0,3)+number.substring(4, number.length());
                        number=temp;
                    }
                    String pass=password.getEditText().getText().toString();



                    User user=new User(name,u_name,mail,selected_country_code,number,pass);
                    reference.child(user.getUsername()).setValue(user);

                    Toast.makeText(getApplicationContext(),"You are successfully registered. Signin to continue!",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void back_to_login(View view) {
        Intent intent=new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


}


