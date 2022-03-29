package com.example.project_chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class PersonalDetails extends AppCompatActivity {
    private EditText Name_ET, AboutET, PhonenoET;
    private Button SignupButton;
    private FirebaseAuth mAuth;
    String name, about, phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
    }
}