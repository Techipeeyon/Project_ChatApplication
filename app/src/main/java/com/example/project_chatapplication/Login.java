package com.example.project_chatapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String email, password;
    private EditText EmailET, PasswordET;
    private Button LoginButton;
    private TextView ForgotLink,tvLogin;
    private ImageButton signupLink;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        EmailET = findViewById(R.id.email_et);
        PasswordET = findViewById(R.id.password_et);
        LoginButton = findViewById(R.id.login_button);
        tvLogin = findViewById(R.id.tvLogin);
        ForgotLink = findViewById(R.id.tvForgot);
        signupLink = findViewById(R.id.btRegister);

        LoginButton.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                email = EmailET.getText().toString();
                password = PasswordET.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(Login.this, "Please fill all the credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)))
                {
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        LoginButton.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.green));
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(Login.this, "Authenticated successfully", Toast.LENGTH_SHORT).show();
                                        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
                                        usersRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists())
                                                {
                                                    Intent mainIntent = new Intent(Login.this, Chat.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);
                                                } else {
                                                    Intent mainIntent = new Intent(Login.this, PersonalDetails.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                    else
                                    {
                                      /*  progressBar.setVisibility(View.GONE);
                                        LoginButton.setBackgroundTintList(null);*/

                                        String message = task.getException().getMessage();
                                        Toast.makeText(Login.this, "Error occured. "+message, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });


        ForgotLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                email = EmailET.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(Login.this, "Please provide your email address to reset your password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Login.this, "Password reset link has been sent to your inbox.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(Login.this, "Error occurred. "+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent =  new Intent(Login.this, Signup.class);
                signupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signupIntent);
            }
        });

    }


    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(Login.this, Profile.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClick(View v) {
        if (v==LoginButton){
            Intent intent   = new Intent(Login.this,Signup.class);
            Pair[] pairs    = new Pair[1];
            pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
            startActivity(intent,activityOptions.toBundle());
        }
    }
}