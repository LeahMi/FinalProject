package com.dvora.finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;
import android.widget.TextView;

import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private Repository repo = new Repository();
    private Stack<String> prevTitle = new Stack<>();
    private TextView actionBarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBarTitle = findViewById(R.id.actionBarTitle);
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottomNavigationView) ;
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.profileFragment, R.id.listRecipesFragment2,R.id.categoriesFragment, R.id.inventoryFragment, R.id.ListShoppingFragment )
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        getSupportActionBar().hide();
        //changeActionBarTitle("קטגוריות" ,true);
    }

    public void changeActionBarTitle(String title,boolean backButton) {
        if(!backButton)
            prevTitle.push(title);

        actionBarTitle.setText(title);
    }

    @Override
    public boolean onSupportNavigateUp() {
        System.out.println("Back");
        return navController.navigateUp();
    }


    @Override
    public void onBackPressed() {
        //changeActionBarTitle(prevTitle.pop(),true);
        System.out.println(prevTitle.size());
        super.onBackPressed();
    }
}
