package com.dvora.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
 import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dvora.finalproject.FirebaseManager;
import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.entities.Category;
import com.dvora.finalproject.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Register extends BaseActivity {
    EditText mFullName, mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    private String fullName;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USER = "user";

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        getSupportActionBar().hide();
        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBarRegister);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.loginTextView);
        fAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child(USER);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                fullName = mFullName.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("נדרש דואר אלקטרוני");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("נדרשת סיסמא");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("סיסמא חייבת לכלול לפחות 6 תווים");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                User user1 = new User(email, password, fullName);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // send verification link

                            FirebaseUser user = fAuth.getCurrentUser();

                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(Register.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", "mail sent");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "OnFailure: Email Not Sent" + e.getMessage());
                                }
                            });

                            Toast.makeText(Register.this, "החשבון נוצר", Toast.LENGTH_SHORT).show();


//                            String keyId=mDatabase.push().getKey();
//                            String currentuser = user.getUid();
//                            mDatabase.child(currentuser).setValue(user1);
                            Log.d("TAG", "updateUI====Before====");
                            updateUI(user);
                            Log.d("TAG", "updateUI====After====");

                        } else {
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                finish();
            }
        });
    }

    public void updateUI(FirebaseUser currentUser) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(currentUser.getUid());
        Log.d("TAG", "currentUser==========" + currentUser);
        String keyId = currentUser.getUid();
        Log.d("TAG", "keyId==========" + keyId);
        String in = "0";
        ref.child("userName").setValue(fullName);

        FirebaseManager.resetAfterLogin();
        FirebaseManager.setAllergens();




        FirebaseDatabase.getInstance().getReference().child("base_vategories_for_all_users").child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Repository repository = new Repository();
//                HashMap<String,HashMap<String,Category>> categoryHashMap= (HashMap<String,HashMap<String,Category>>) snapshot.getValue( );
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    Log.v("asdas", category.getName());
                    repository.addCategory(category,null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        },1500);



//        ref.child(keyId).child("Details").setValue(currentUser);
    }


}