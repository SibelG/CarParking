package com.example.caparking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.caparking.Helper.DBHelper;
import com.example.caparking.util.SessionManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Login extends AppCompatActivity {

    @Inject
    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.pass);
        Button signin = findViewById(R.id.login);
        Button back = findViewById(R.id.button04);
        Button signup = findViewById(R.id.signup);
        DBHelper DB = new DBHelper(this);

        manager = new SessionManager(getApplicationContext());

        //checks the login status and redirects to the main activity
        if (manager.isLoggedIn()) {

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
            finish();
            return;


        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String pass = password.getText().toString();

                if (userEmail.equals("") || pass.equals(""))
                    Toast.makeText(Login.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkuseremailpassword(userEmail, pass);
                    if (checkuserpass==true){

                        int id = DB.GetId(userEmail);
                        manager.createLoginSession(id);
                        Toast.makeText(Login.this, String.valueOf(id), Toast.LENGTH_SHORT).show();

                        Toast.makeText(Login.this, "LogIn Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            }
        });





    }

    private void replaceFragment (){
        MapsFragment newFragment = new MapsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.nav_host_fragment, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}