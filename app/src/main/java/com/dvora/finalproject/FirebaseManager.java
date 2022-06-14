package com.dvora.finalproject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {
    public static DatabaseReference root= FirebaseDatabase.getInstance().getReference();
    public static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();



    public static void resetAfterLogin(){
        root= FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

}
