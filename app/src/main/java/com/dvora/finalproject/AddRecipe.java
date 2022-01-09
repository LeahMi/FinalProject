package com.dvora.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddRecipe extends AppCompatActivity {
    private static int Id=-1;
    private Button btn;

    private EditText nameRecipe;
    private EditText category;
    private EditText preparationTime;
    private EditText ingredients;
    private String Ingredient;

    private EditText preparationMethod;
    private DatabaseReference rootDatabaseRef;
//    private FirebaseDatabase database;
//    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        RecipeRepository repo = new RecipeRepository();
//        repo.getRecipesData().observe(this, recipes -> {
//            // Code when change is happenning to firebase 'recipes'
//        });
//        repo.getExceptionsData().observe(this,error -> {
//            // errors happend , inform the user
//        });
        nameRecipe=findViewById(R.id.nameRecipe);
        category= findViewById(R.id.category);
        preparationTime= findViewById(R.id.preparationTime);
        ingredients= findViewById(R.id.ingredients);
        preparationMethod= findViewById(R.id.preparationMethod);
        btn= findViewById(R.id.addRecipeBtn);

        //database =FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user= mAuth.getCurrentUser();
        String keyId=user.getUid();
        rootDatabaseRef= FirebaseDatabase.getInstance().getReference().child("user").child(keyId).child("Recipes");


        btn.setOnClickListener((v) ->{
            Log.d("TAG","before String");

            String NameRecipe=nameRecipe.getText().toString();
            String Category=category.getText().toString();
            String PreparationTime=preparationTime.getText().toString();
            String Ingredients1=ingredients.getText().toString();

            String str[] = Ingredients1.split("\\R");
            List<String> al = new ArrayList<String>();
            al = Arrays.asList(str);

            Log.d("TAG","al==== "+al);

            String PreparationMethod=preparationMethod.getText().toString();
            Log.d("TAG","Id:"+Id);
            ++Id;
            Log.d("TAG","Id:"+Id);

            Log.d("TAG","After String");

            HashMap hashMap= new HashMap();


            hashMap.put("Name Recipe",NameRecipe);
            hashMap.put("Category",Category);
            hashMap.put("Preparation Time",PreparationTime);
            hashMap.put("Ingredients",al);
            hashMap.put("Preparation Method",PreparationMethod);

            Log.d("TAG","After HashMap");

            Log.d("TAG","Id: "+Id);
            rootDatabaseRef.child(String.valueOf(Id)).setValue(hashMap).addOnSuccessListener((OnSuccessListener)(aVoid)->{
                Toast.makeText(AddRecipe.this,"The recipe is successfully added",Toast.LENGTH_SHORT).show();
                Log.d("TAG","Recipe added");
            }).addOnFailureListener((e)->{
                Toast.makeText(AddRecipe.this,"Sorry permission denied",Toast.LENGTH_SHORT).show();
                Log.d("TAG","Recipe not added");
            });

        });

    }
}