package com.dvora.finalproject.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.entities.Ingredient;


public class DialogIng extends DialogFragment {

    public static final String TAG = "MyCustomDialog";


    public interface OnInputSelected {
        void sendInput(String input);
    }


    private EditText mInput;
    private Repository repo = new Repository();
    private EditText et_name;
    private EditText et_quantity;
    private Spinner et_type;
    private String Name;
    private String Quantity;
    private String[] Type = {"גרם","מל","יחידה"};
    private Ingredient ingredient;
    private TextView mActionOk, mActionCancel;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
        Context context = getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        alert.setTitle("צור מרכיב חדש במלאי                        ");
        et_name = new EditText(context);
        et_name.setHint("מרכיב");
        layout.addView(et_name);
        et_quantity = new EditText(context);
        et_quantity.setHint("כמות");
        layout.addView(et_quantity);
        et_type = new Spinner(context);
        layout.addView(et_type);
        alert.setView(layout);

        alert.setPositiveButton("הוסף", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Name = et_name.getText().toString().trim();
                Quantity = et_quantity.getText().toString().trim();
                Double q = Double.parseDouble(Quantity);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Type);
                et_type.setAdapter(adapter);
                et_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.v("item", (String) parent.getItemAtPosition(position));
                        Type = (String[]) parent.getItemAtPosition(position);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                ingredient = new Ingredient(Name, q, null, Type);
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
        alert.setNegativeButton("בטל", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        return alert.show();

    }
    public String getNameIng(){
        return Name;
    }

}
