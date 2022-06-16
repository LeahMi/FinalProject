package com.dvora.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.dvora.finalproject.R;
import com.dvora.finalproject.fragments.ICallbackFragment;;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Stack;


public class MainActivity extends BaseActivity implements ICallbackFragment {

    public static String sort = "null";
    private NavController navController;
    private Stack<String> prevTitle = new Stack<>();
    private TextView actionBarTitle;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBarTitle = findViewById(R.id.actionBarTitle);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);
        navController.setGraph(navGraph);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == bottomNavigationView.getSelectedItemId()) {
                    showFragment(item.getItemId());
                    return false;
                } else {
                    showFragment(item.getItemId());
                    return true;
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profileFragment:
                Log.v("pppp","pppro");
                showFragment(R.id.profileFragment);
                return true;
            case R.id.inventoryFragment:
                Log.v("innnnn","innnnv");
                showFragment(R.id.inventoryFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeActionBarTitle(String title, boolean backButton) {
        if (!backButton)
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

    @Override
    public void showFragment(int fragmentID) {
        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(fragmentID);
    }

    @Override
    public void showFragment(int fragmentID, Bundle bundle) {
        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(fragmentID, bundle);
    }
}

