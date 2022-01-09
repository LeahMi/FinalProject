package com.dvora.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddRecipeFragment extends Fragment {
    //private Button btnAdd;
//    private EditText nameRecipe;
//    private EditText category;
//    private EditText preparationTime;
//    private EditText ingredients;
//    private EditText preparationMethod;
    private String nameIngredient;
    private double quantityIngredient;

    private RecipeRepository repo = new RecipeRepository();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        IngredientInfo ing = new IngredientInfo("Pepper",5.5);
//        List<IngredientInfo> allIngredients = new ArrayList<>();
//        allIngredients.add(ing);
//        Recipe recipe = new Recipe("Chicken","Chicken","200",allIngredients,"Deep cook");
//        repo.saveNewRecipe(recipe, new RecipeRepository.OnAddNewRecipeListener() {
//            @Override
//            public void onSuccess(String message) {
//                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
//                Log.d("saveNewRecipe::Succeed",message);
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                Log.d("saveNewRecipe::Failure",e.getLocalizedMessage());
//            }
//        });
//        repo.getRecipesForIngredient("Pepper", new RecipeRepository.OnSearchRecipesByIngredient() {
//            @Override
//            public void onRecipesFound(List<Recipe> matches) {
//                Toast.makeText(getContext(),matches.size() + " Recipes Found!",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNoRecipesFound(String message) {
//                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onExceptionOcured(Exception e) {
//                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_add_recipe, container, false);
        EditText nameRecipe= (EditText) v.findViewById(R.id.nameRecipe);
        EditText category= (EditText)v.findViewById(R.id.category);
        EditText preparationTime= (EditText)v.findViewById(R.id.preparationTime);
        EditText ingredients= (EditText)v.findViewById(R.id.ingredients);
        EditText preparationMethod= (EditText) v.findViewById(R.id.preparationMethod);
        Button btnAdd= (Button) v.findViewById(R.id.addRecipeBtn);

        btnAdd.setOnClickListener((vi)->{
            String NameRecipe= nameRecipe.getText().toString();
            String Category= category.getText().toString();
            String PreparationTime= preparationTime.getText().toString();
            String PreparationMethod= preparationMethod.getText().toString();
            String Ingredients= ingredients.getText().toString();

            String str[] = Ingredients.split("\\R");

            Log.d("TAG","str====="+str);
            IngredientInfo ing;
            List<IngredientInfo> allIngredients = new ArrayList<>();
            for (String s: str) {
                Log.d("TAG","s====="+s);
                String ing1[]= s.split("\\s+");
                Log.d("TAG","ing1====="+ing1);
                ing= new IngredientInfo(ing1[0],Double.parseDouble(ing1[1]));
                Log.d("TAG","ing===="+ing);
                allIngredients.add(ing);
            }
            Recipe recipe = new Recipe(NameRecipe,Category,PreparationTime,allIngredients,PreparationMethod);
            repo.saveNewRecipe(recipe, new RecipeRepository.OnAddNewRecipeListener() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                    Log.d("saveNewRecipe::Succeed",message);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.d("saveNewRecipe::Failure",e.getLocalizedMessage());
                }
            });
            Intent intent=new Intent();
            intent.setClass(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
        });

        return v;

    }
}