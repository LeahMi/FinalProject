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

    public OnInputSelected mOnInputSelected;

    //widgets
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
        //alert.setMessage("Enter a new ingredient");
        alert.setTitle("Add New Ingredient");
        et_name = new EditText(context);
        et_name.setHint("ingredient");
        layout.addView(et_name);
        et_quantity = new EditText(context);
        et_quantity.setHint("quantity");
        layout.addView(et_quantity);
        et_type = new EditText(context);
        et_type.setHint("ml/gr/pcs");
        layout.addView(et_type);
        alert.setView(layout);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                Name = et_name.getText().toString().trim();
                Quantity = et_quantity.getText().toString().trim();
                Double q = Double.parseDouble(Quantity);
                Type = et_type.getText().toString().trim();
                ingredient = new Ingredient(Name,q, null,Type);
                repo.saveNewIngredient(ingredient, new Repository.OnAddNewIngredientListener() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d("saveNewIng::Succeed",message);
                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("saveNewIng::Failure",e.getLocalizedMessage());
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        return alert.show();
//        return new AlertDialog.Builder(requireContext())
//                .setMessage(getString(R.string.app_name))
//                .setPositiveButton(getString(R.string.previous), (dialog, which) -> {} )
//                .create();
    }
    public String getNameIng(){
        return Name;
    }
//    @Nullable
//    @Override
//    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_dialog_ing, container, false);
//        mActionOk = view.findViewById(R.id.action_ok);
//        mActionCancel = view.findViewById(R.id.action_cancel);
//        mInput = view.findViewById(R.id.input);
//
//        mActionCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: closing dialog");
//                getDialog().dismiss();
//            }
//        });
//
//        mActionOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: capturing input.");
//
//                String input = mInput.getText().toString();
//                if(!input.equals("")){
////
////                    //Easiest way: just set the value.
////                    MainFragment fragment = (MainFragment) getActivity().getFragmentManager().findFragmentByTag("MainFragment");
////                    fragment.mInputDisplay.setText(input);
//
//                    mOnInputSelected.sendInput(input);
//                }
//
//
//                getDialog().dismiss();
//            }
//        });
//
//        return view;
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try{
//            mOnInputSelected = (OnInputSelected) getTargetFragment();
//        }catch (ClassCastException e){
//            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
//        }
//    }
}
