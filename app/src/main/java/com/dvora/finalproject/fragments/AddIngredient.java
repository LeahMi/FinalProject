package com.dvora.finalproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.entities.Ingredient;


public class AddIngredient extends Fragment {

    private String type;
    private EditText name;
    private EditText amount;
    private Button buttonSave;
    private String[] types = {"gr","ml","psc"};
    private Repository repo = new Repository();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_ingredient, container, false);

        Spinner spinner =(Spinner) v.findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                type = (String)parent.getItemAtPosition(position);
                //((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        amount = (EditText) v.findViewById(R.id.amount);
        name = (EditText) v.findViewById(R.id.nameIngredient);
        buttonSave = (Button) v.findViewById(R.id.button_Save_Ing);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString().trim();
                String Amount = amount.getText().toString().trim();
                Ingredient ingredient = new Ingredient(Name,Double.parseDouble(Amount), null,type);
                repo.saveNewIngredient(ingredient, new Repository.OnAddNewIngredientListener() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d("saveNewIng::Succeed",message);
                        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("saveNewIng::Failure",e.getLocalizedMessage());
                    }
                });
            }
        });
        return v;
    }

}