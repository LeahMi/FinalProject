
package com.dvora.finalproject.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dvora.finalproject.Repository;
import com.dvora.finalproject.entities.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DialogIng extends DialogFragment {

    public static final String TAG = "MyCustomDialog";


    public interface OnInputSelected {
        void sendInput(String input);
    }


    private EditText mInput;
    private Repository repo = new Repository();
    private EditText et_name;
    private EditText et_type;
    private Spinner spinner;
    private String[] types = {"בחר סוג","גרם","מל","יחידה"};
    List<String> spinnerList;
    ArrayAdapter<String> arrayAdapter;
    private String Name;
    private String Type;
    private Ingredient ingredient;
    private TextView mActionOk, mActionCancel;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
        Context context = getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        alert.setTitle("מוצר חדש");
        et_name = new EditText(context);
        et_name.setHint("שם");
        layout.addView(et_name);
        spinnerList = new ArrayList<>(Arrays.asList(types));
        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinnerList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    //Disable the first item of spinner.
                    return false;
                }
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View spinnerview = super.getDropDownView(position, convertView, parent);

                TextView spinnertextview = (TextView) spinnerview;

                if(position == 0) {
                    //Set the disable spinner item color fade .
                    spinnertextview.setTextColor(Color.parseColor("#bcbcbb"));
                }
                else
                    spinnertextview.setTextColor(Color.BLACK);
                return spinnerview;
            }
        };

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinner.setAdapter(arrayAdapter);
        /////////
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                Type = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        layout.addView(spinner);
        et_type = new EditText(context);
        et_type.setHint("יחידה/ליטר/גרם");
        layout.addView(et_type);
        alert.setView(layout);

        alert.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Name = et_name.getText().toString().trim();
                Type = et_type.getText().toString().trim();
                ingredient = new Ingredient(Name, 0.0, null, Type);
                repo.saveNewIngredient(ingredient, new Repository.OnAddNewIngredientListener() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d("saveNewIng::Succeed", message);
                        //mOnInputSelected.sendInput(Name);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("saveNewIng::Failure", e.getLocalizedMessage());
                    }
                });
            }
        });

        alert.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        return alert.show();

    }

    public String getNameIng() {
        return Name;
    }

}