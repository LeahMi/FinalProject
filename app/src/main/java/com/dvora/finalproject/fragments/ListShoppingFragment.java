package com.dvora.finalproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dvora.finalproject.R;
import com.dvora.finalproject.RecipeRepository;
import com.dvora.finalproject.entities.Ingredient;

public class ListShoppingFragment extends Fragment {

    private EditText testEditText;
    private Button buttonSave;
    private RecipeRepository repo = new RecipeRepository();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_shopping, container, false);
        testEditText = v.findViewById(R.id.testEditText);
        buttonSave = (Button) v.findViewById(R.id.button_Save_Ing);
        repo.getList(new RecipeRepository.OnSearchShoppingList() {
            @Override
            public void onSuccess(String message) {
                if(message == "Enter your Shopping List")
                {
                    testEditText.setHint(message);
                }
                else {testEditText.setText(message);}
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String ListS = testEditText.getText().toString().trim();
                repo.SaveListShopping(ListS);

            }
        });
        return v;

    }
}

