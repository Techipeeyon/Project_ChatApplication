package com.example.project_chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PersonalDetails extends AppCompatActivity {
    private EditText Name_ET, AboutET, PhonenoET, DoBET;
    private Button createButton;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private String uid;

    String name, about, phoneno, DoB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        mAuth = FirebaseAuth.getInstance();

        Name_ET = findViewById(R.id.name);
        AboutET = findViewById(R.id.about);
        PhonenoET = findViewById(R.id.phoneno);
        DoBET = findViewById(R.id.DoB);
        createButton = findViewById(R.id.createButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                name = Name_ET.getText().toString();
                about = AboutET.getText().toString();
                phoneno = PhonenoET.getText().toString();
                DoB = DoBET.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(about) || TextUtils.isEmpty(phoneno) || TextUtils.isEmpty(DoB))
                {
                    Toast.makeText(PersonalDetails.this,"All the fields are mandatory",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(TextUtils.isEmpty(name)&&TextUtils.isEmpty(phoneno)&&TextUtils.isEmpty(about)))
                {

                    usersRef = FirebaseDatabase.getInstance().getReference("Users");
                    uid = mAuth.getCurrentUser().getUid();
                    HashMap<String,Object> result=new HashMap<>();
                    result.put("Name",name);
                    result.put("About",about);
                    result.put("PhoneNo",phoneno);
                    result.put("DoB",DoB);


                    usersRef.child(uid).updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(PersonalDetails.this, "Wow! We are so close now", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(PersonalDetails.this, Login.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(PersonalDetails.this, "Error occurred. "+message, Toast.LENGTH_SHORT).show();
                                return;
                            }


            }

            });


    }
}});
    }}