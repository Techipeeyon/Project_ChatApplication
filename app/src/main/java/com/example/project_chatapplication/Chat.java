package com.example.project_chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Chat<mAuth> extends AppCompatActivity {
    private Button profileButton, logoutbutton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();

        profileButton = findViewById(R.id.profileButton);
        logoutbutton = findViewById(R.id.logoutbutton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(Chat.this,Profile.class);
                startActivity(mainIntent);
            }
        });
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                Intent mainIntent = new Intent(Chat.this,Login.class);
                startActivity(mainIntent);
            }
        });
    }
}