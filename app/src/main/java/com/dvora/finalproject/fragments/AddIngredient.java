package com.dvora.finalproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
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


public class AddIngredient extends BaseFragment {

    private String type;
    private EditText name;
    private EditText amount;
    private Button buttonSave;
    private String[] types = {"גרם","מל","יחידה"};
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
                if(TextUtils.isEmpty(Name) && TextUtils.isEmpty(Amount)){
                    name.setError("הזן מוצר");
                    amount.setError("הזן כמות");
                    return;
                }
                if(TextUtils.isEmpty(Name)){
                    name.setError("הזן מוצר");
                    return;
                }
                if(TextUtils.isEmpty(Amount)){
                    amount.setError("הזן כמות");
                    return;
                }
                Ingredient ingredient = new Ingredient(Name,Double.parseDouble(Amount), null,type);
                repo.saveNewIngredient(ingredient, new Repository.OnAddNewIngredientListener() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d("saveNewIng::Succeed",message);
                        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
//                        if(TextUtils.isEmpty(amount.getText().toString().trim())) {
//                            amount.setError("הזן כמות");
//                            return;
//                        }
                        mListener.showFragment(R.id.inventoryFragment);

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