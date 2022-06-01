
package com.dvora.finalproject.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText et_type;
    private String Name;
    private String Quantity;
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
        alert.setTitle("׳¦׳•׳¨ ׳�׳¨׳›׳™׳‘ ׳—׳“׳© ׳‘׳�׳�׳�׳™                        ");
        et_name = new EditText(context);
        et_name.setHint("׳�׳¨׳›׳™׳‘");
        layout.addView(et_name);
        et_quantity = new EditText(context);
        et_quantity.setHint("׳›׳�׳•׳×");
        layout.addView(et_quantity);
        et_type = new EditText(context);
        et_type.setHint("׳�׳�/׳™׳—׳™׳“׳”/׳’׳¨׳�");
        layout.addView(et_type);
        alert.setView(layout);

        alert.setPositiveButton("׳”׳•׳¡׳£", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Name = et_name.getText().toString().trim();
                Quantity = et_quantity.getText().toString().trim();
                Double q = Double.parseDouble(Quantity);
                Type = et_type.getText().toString().trim();
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

        alert.setNegativeButton("׳‘׳˜׳�", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        return alert.show();

    }

    public String getNameIng() {
        return Name;
    }

}