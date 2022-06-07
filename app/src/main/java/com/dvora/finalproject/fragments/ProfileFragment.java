package com.dvora.finalproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.activities.Login;
import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.entities.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;


public class ProfileFragment extends Fragment {
    private Repository repo = new Repository();
    public DatabaseReference ref;
    private TextView Name;
//    private TextView Mail;
    private String FullName;
    private Button logOut;
    public ProfileFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        logOut = (Button) v.findViewById(R.id.log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), Login.class));
            }
        });

        //((MainActivity)getActivity()).changeActionBarTitle("פרופיל",false);
        Name= (TextView) v.findViewById(R.id.texthello);
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