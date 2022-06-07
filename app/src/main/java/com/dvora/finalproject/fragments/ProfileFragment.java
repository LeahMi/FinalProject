package com.dvora.finalproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dvora.finalproject.FirebaseManager;
import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.activities.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import static com.google.firebase.auth.FirebaseAuth.*;


public class ProfileFragment extends Fragment {
    private Repository repo = new Repository();
    public DatabaseReference ref;
    private TextView Name;
    private String FullName;
    private Button logOut;

    public ProfileFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        //((MainActivity)getActivity()).changeActionBarTitle("פרופיל",false);
        Name= (TextView) v.findViewById(R.id.texthello);
        logOut = (Button) v.findViewById(R.id.log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), Login.class));
            }
        });
        Name.setText("שלום "+ FullName);
        repo.getProfile(new Repository.OnSearchProfile() {
            @Override
            public void onSuccess(String message) {
                Name.setText("שלום "+ message);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        return v;
    }
}