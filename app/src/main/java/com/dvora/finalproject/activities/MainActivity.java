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


    }
}
