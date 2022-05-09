package com.dvora.finalproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dvora.finalproject.ListRecipesFragment;
import com.dvora.finalproject.R;
import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.entities.IngredientInfo;
import com.dvora.finalproject.entities.Recipe;

import java.util.List;

public class ItemDetailsFragment extends Fragment implements View.OnClickListener {


    public final static String RECIPE_KEY = "RECIPE_KEY";
    private Recipe recipe;
    private Button btn_updateInventory;
    private ImageButton btn_back;

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

        final EditText categoryEt = view.findViewById(R.id.edt_category);
        final EditText nameEt = view.findViewById(R.id.edt_name);
        final EditText timeEt = view.findViewById(R.id.edt_time);
        final EditText prepEt = view.findViewById(R.id.edt_prep_method);
        final EditText IngEt = view.findViewById(R.id.edt_ing);
        btn_updateInventory = view.findViewById(R.id.button);
        btn_back = view.findViewById(R.id.btn_back);
        btn_updateInventory.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        categoryEt.setText(recipe.getCategory());
        nameEt.setText(recipe.getNameRecipe());
        timeEt.setText(recipe.getPreparationTime());
        prepEt.setText(recipe.getPreparationMethod());
        List<IngredientInfo> list = recipe.getIngredients();
        String strList = "";
        for (int i = 0; i < list.size(); ++i) {
            if(i==list.size()-1)
                strList = strList + list.get(i).getName()+" "+list.get(i).getQuantity()+" "+list.get(i).getType();
            else
                strList = strList + list.get(i).getName()+" "+list.get(i).getQuantity()+" "+list.get(i).getType()+ '\n';
        }
        IngEt.setText(strList);
        return view;
    }

    public void showFragment(Fragment frag) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.replace(R.id.fragment, frag);
        tran.commit();
    }

    @Override
    public void onClick(View v) {
        if(v == btn_back){
            showFragment(new ListRecipesFragment());
        }
        if(v ==btn_updateInventory){
            Toast.makeText(getContext(),"Clicked!",Toast.LENGTH_SHORT).show();
        }
    }
}