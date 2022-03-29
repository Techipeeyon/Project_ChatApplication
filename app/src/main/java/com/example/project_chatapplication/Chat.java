package com.example.project_chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Chat extends AppCompatActivity {
    private Button profileButton, logoutbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        profileButton = findViewById(R.id.profileButton);
        logoutbutton = findViewById(R.id.logoutbutton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(Chat.this,Profile.class);
                startActivity(mainIntent);
            }
        });
    }
}