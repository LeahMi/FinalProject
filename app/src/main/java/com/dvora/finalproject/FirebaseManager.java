package com.dvora.finalproject;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseManager {
    public static DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    public static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    public static List<String> allergens = null;


    public static void resetAfterLogin(){
        root = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }
    public static void setAllergens(){
        Repository repo = new Repository();
        repo.getAllergens(new Repository.OnSearchAllAllergens() {
            @Override
            public void onAllergensFound(List<String> matches) {
                 allergens = copy(matches);
            }
            @Override
            public void onNoAllergensFound(String message) { allergens = null; }
            @Override
            public void onExceptionOccurred(Exception e) { }
        });
    }

    private static List<String> copy(List<String> l){

        List<String> newList = new ArrayList<>(l.size());
        for (String s: l){
            newList.add(s);
        }
        Log.e("InCopy","newList ----- "+ newList);
        return newList;
    }


}
