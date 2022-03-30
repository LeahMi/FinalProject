package com.dvora.finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;
import android.widget.Button;

import com.dvora.finalproject.R;
import com.dvora.finalproject.RecipeRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottomNavigationView) ;
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.profileFragment, R.id.listRecipesFragment2, R.id.inventoryFragment)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


//        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),Login.class));
//            logout(view);
//            }
//        });
//        mLogoutBtn=findViewById(R.id.logoutBtn);
//        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(getApplicationContext(),Login.class));
//                finish();
//            }
//        });
//        mAddRecipeBtn=findViewById(R.id.addRecipeOptionBtn);
//        mAddRecipeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Log.d("TAG","AddRecipe class");
////                startActivity(new Intent(getApplicationContext(),AddRecipe.class));
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,new AddRecipeFragment()).commit();
//                mAddRecipeBtn.setVisibility(View.GONE);
//                mAllRecipesBtn.setVisibility(View.GONE);
//                mLogoutBtn.setVisibility(View.GONE);
//            }
//        });
//        mAllRecipesBtn= findViewById(R.id.allRecipesOptionBtn);
//        mAllRecipesBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,new ListRecipesFragment()).commit();
//                mAddRecipeBtn.setVisibility(View.GONE);
//                mAllRecipesBtn.setVisibility(View.GONE);
//                mLogoutBtn.setVisibility(View.GONE);
//            }
//        });
    }

//    public void logout(View view){
//
//    }
}
