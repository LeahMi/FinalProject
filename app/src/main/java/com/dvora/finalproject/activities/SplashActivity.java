package com.dvora.finalproject.activities;

import android.annotation.SuppressLint;



import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;

import com.dvora.finalproject.R;

import com.dvora.finalproject.fragments.CategoriesFragment;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Reposetory.getInstance(this).clearFavoriteList();
        showScreen();
    }

    private void showScreen() {
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                CategoriesFragment fragment = new Fragment();
//                FragmentManager manager = getFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.add(R.id.container,fragment,"tag");
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        }, 3000);
    }
}