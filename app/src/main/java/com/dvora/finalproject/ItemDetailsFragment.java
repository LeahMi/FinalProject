package com.dvora.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;

public class ItemDetailsFragment extends Fragment {


    public final static String RECIPE_KEY = "RECIPE_KEY";
    private Recipe recipe;

    public static ItemDetailsFragment newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putSerializable(RECIPE_KEY, recipe);
        ItemDetailsFragment fragment = new ItemDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = getArguments();
        recipe = (Recipe) bundle.getSerializable(RECIPE_KEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_details, container, false);

        final EditText idEt = view.findViewById(R.id.edt_id);
        final EditText nameEt = view.findViewById(R.id.edt_name);
        final EditText phoneEt = view.findViewById(R.id.edt_phone);
        final EditText addressEt = view.findViewById(R.id.edt_address);

        Button btn_edit = view.findViewById(R.id.btn_delete);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new ListRecipesFragment());
            }
        });
        idEt.setText(recipe.getCategory());
        nameEt.setText(recipe.getNameRecipe());
        phoneEt.setText(recipe.getPreparationTime());
        addressEt.setText(recipe.getPreparationMethod());
        return view;
    }

    public void showFragment(Fragment frag) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.replace(R.id.container, frag);
        tran.commit();
    }

}