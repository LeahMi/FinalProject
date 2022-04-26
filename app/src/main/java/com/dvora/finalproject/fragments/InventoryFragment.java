package com.dvora.finalproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dvora.finalproject.AddRecipeFragment;
import com.dvora.finalproject.IngAdapter;
import com.dvora.finalproject.R;
import com.dvora.finalproject.RecipeRepository;
import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.entities.Ingredient;
import com.dvora.finalproject.entities.IngredientInfo;
import com.dvora.finalproject.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {
    private AutoCompleteTextView textIn;
    private ListView list;
    private Button buttonAdd;
    private Button ListShopping;
    private ArrayAdapter<String> arrayAdapter;
    String[] nameList = {};

    private RecipeRepository repo = new RecipeRepository();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventory, container, false);
        repo.getAllIngredients(new RecipeRepository.OnSearchAllIngredients() {
            public void onIngredientsFound(List<Ingredient> matches){
                Toast.makeText(getContext(),matches.size() + " Ingredients Found!",Toast.LENGTH_SHORT).show();
                list= v.findViewById(R.id.mainlistinventory_listv);
                int i=0;
                nameList=new String[matches.size()];
                for (Ingredient ingredient:matches) {
                    nameList[i]=ingredient.getName();
                    ++i;
                }
                //  arrayAdapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,android.R.id.text1,nameList);
                IngAdapter adapter= new IngAdapter(matches, getContext());
                list.setAdapter(adapter);
            }
            // @Override
//            public void onIngredientsFound(List <Ingredient>) {
//
//            }

            @Override
            public void onNoIngredientsFound(String message) {
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onExceptionOccurred(Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        //ScrollView InventoryScroll = (ScrollView) v.findViewById(R.id.InventoryScroll);
        // textIn = v.findViewById(R.id.textin);
        ListShopping=v.findViewById(R.id.btn_list_shopping);
        ListShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(new ListShoppingFragment());
            }
        });

        buttonAdd=v.findViewById(R.id.btn_Add_ing);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(new AddIngredient());
            }
        });

        return v;
    }
    public void showFragment(Fragment frag) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.replace(R.id.fragment, frag);
        tran.commit();
    }
}