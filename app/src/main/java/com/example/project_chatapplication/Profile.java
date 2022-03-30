package com.example.project_chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView Name, About, phoneno, DoB;
    private DatabaseReference userref;
    private Button logoutbutton;
    String email;
    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        Name = findViewById(R.id.name);
        About = findViewById(R.id.about);
        phoneno = findViewById(R.id.phoneno);
        DoB = findViewById(R.id.DoB);
        logoutbutton = findViewById(R.id.logoutbutton);

        mAuth = FirebaseAuth.getInstance();

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                Intent mainIntent = new Intent(Profile.this,Login.class);
                startActivity(mainIntent);
            }
        });
        email = getIntent().getStringExtra("email");
        currentUserID = mAuth.getCurrentUser().getUid();
        userref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        userref.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String UName = snapshot.child("Name").getValue().toString();
                    Name.setText(UName);
                }
                else{
                    Name.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {


            }
        });
    }
}