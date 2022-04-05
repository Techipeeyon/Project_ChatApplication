package com.example.project_chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText Name, About, phoneno, DoB;
    private DatabaseReference usersRef;
    private Button logoutbutton, Savebutton;
    String email;
    String currentUserID;
    String name, about, Phoneno, Dob;
    private String uid;



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
        Savebutton = findViewById(R.id.saveChangesbutton);

        mAuth = FirebaseAuth.getInstance();
if (mAuth.getCurrentUser()!= null){
    usersRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
    usersRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Name.setText(snapshot.child("Name").getValue().toString());
            DoB.setText(snapshot.child("DOB").getValue().toString());
            About.setText(snapshot.child("About").getValue().toString());
            phoneno.setText(snapshot.child("Phone No").getValue().toString());
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}
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
        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        usersRef.child(email).addValueEventListener(new ValueEventListener() {
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

        Savebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                name = Name.getText().toString();
                about = About.getText().toString();
                Phoneno = phoneno.getText().toString();
                Dob = DoB.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(about) || TextUtils.isEmpty(Phoneno) || TextUtils.isEmpty(Dob))
                {
                    Toast.makeText(Profile.this,"All the fields are mandatory",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(TextUtils.isEmpty(name)&&TextUtils.isEmpty(Phoneno)&&TextUtils.isEmpty(about))) {

                    usersRef = FirebaseDatabase.getInstance().getReference("Users");
                    uid = mAuth.getCurrentUser().getUid();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("Name", name);
                    result.put("About", about);
                    result.put("PhoneNo", phoneno);
                    result.put("DoB", DoB);


                    usersRef.child(uid).updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Profile.this, "Wow! We are so close now", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(Profile.this, Login.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(Profile.this, "Error occurred. " + message, Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }



                    });

    }
}});

                }
}