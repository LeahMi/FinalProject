package com.dvora.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dvora.finalproject.R;
import com.dvora.finalproject.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




public class Register extends AppCompatActivity{
    EditText mFullName, mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    private String fullName;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USER="user";
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFullName= findViewById(R.id.fullName);
        mEmail= findViewById(R.id.email);
        mPassword= findViewById(R.id.password) ;
        progressBar = findViewById (R.id.progressBarRegister);
        mRegisterBtn= findViewById(R.id.registerBtn);
        mLoginBtn= findViewById(R.id.loginTextView);
        fAuth= FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference().child(USER);

        if(fAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                fullName=mFullName.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Recuired.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Recuired.");
                    return;
                }
                if(password.length()<6){
                    mPassword.setError("Password Must be >=6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                User user1= new User(email,password,fullName);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // send verification link

                            FirebaseUser user= fAuth.getCurrentUser();

                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                    Log.d("TAG","mail sent");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG","OnFailure: Email Not Sent"+ e.getMessage());
                                }
                            });

                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();



//                            String keyId=mDatabase.push().getKey();
//                            String currentuser = user.getUid();
//                            mDatabase.child(currentuser).setValue(user1);
                            Log.d("TAG","updateUI====Before====");
                            updateUI(user);
                            Log.d("TAG","updateUI====After====");
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Register.this, "Error!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
    public void updateUI(FirebaseUser currentUser){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(currentUser.getUid());
        Log.d("TAG","currentUser=========="+currentUser);
        String keyId=currentUser.getUid();
        Log.d("TAG","keyId=========="+keyId);
        String in="0";
        ref.child("userName").setValue(fullName);

//        ref.child(keyId).child("Details").setValue(currentUser);
    }

}