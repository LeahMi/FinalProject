package com.dvora.finalproject.fragments;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dvora.finalproject.R;


public class SplashFragment extends Fragment {

    View view;
    public static String sort = "";

    public SplashFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        view = inflater.inflate(R.layout.fragment_splash, container, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_categoriesFragment);

            }
        },3000);
        return view;
    }

}
