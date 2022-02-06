package com.dvora.finalproject;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //private FirebaseAuth mAuth;
    Button mLogoutBtn;
    Button mAddRecipeBtn;
    Button mAllRecipesBtn;
    private RecipeRepository repo = new RecipeRepository();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),Login.class));
//            logout(view);
//            }
//        });
        mLogoutBtn=findViewById(R.id.logoutBtn);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
        mAddRecipeBtn=findViewById(R.id.addRecipeOptionBtn);
        mAddRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("TAG","AddRecipe class");
//                startActivity(new Intent(getApplicationContext(),AddRecipe.class));
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new AddRecipeFragment()).commit();
                mAddRecipeBtn.setVisibility(View.GONE);
                mAllRecipesBtn.setVisibility(View.GONE);
                mLogoutBtn.setVisibility(View.GONE);
            }
        });
        mAllRecipesBtn= findViewById(R.id.allRecipesOptionBtn);
        mAllRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new ListRecipesFragment()).commit();
                mAddRecipeBtn.setVisibility(View.GONE);
                mAllRecipesBtn.setVisibility(View.GONE);
                mLogoutBtn.setVisibility(View.GONE);
            }
        });
    }

//    public void logout(View view){
//
//    }
}
