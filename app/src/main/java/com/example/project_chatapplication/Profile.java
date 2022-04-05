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
import java.util.jar.Attributes;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText Name_et, About_et, phoneno_et, DoB_et;
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

        Name_et = findViewById(R.id.namePro);
        About_et = findViewById(R.id.aboutPro);
        phoneno_et = findViewById(R.id.phonenoPro);
        DoB_et = findViewById(R.id.DoBPro);
        logoutbutton = findViewById(R.id.logoutbutton);
        Savebutton = findViewById(R.id.saveChangesbutton);

        mAuth = FirebaseAuth.getInstance();
if (mAuth.getCurrentUser()!= null){
    String currentUserId = mAuth.getCurrentUser().getUid().toString();
    usersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
    usersRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                String Name = snapshot.child("Name").getValue().toString();
                String DoB = snapshot.child("DoB").getValue().toString();
                String About = snapshot.child("About").getValue().toString();
                String Phoneno = snapshot.child("PhoneNo").getValue().toString();
                Name_et.setText(Name);
                DoB_et.setText(DoB);
                About_et.setText(About);
                phoneno_et.setText(Phoneno);
            }

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


        Savebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                name = Name_et.getText().toString();
                about = About_et.getText().toString();
                Phoneno = phoneno_et.getText().toString();
                Dob = DoB_et.getText().toString();

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
                    result.put("PhoneNo", Phoneno);
                    result.put("DoB", Dob);


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