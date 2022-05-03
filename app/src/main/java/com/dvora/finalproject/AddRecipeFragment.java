package com.dvora.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.entities.Ingredient;
import com.dvora.finalproject.entities.IngredientInfo;
import com.dvora.finalproject.entities.Recipe;
import com.dvora.finalproject.fragments.DialogIng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddRecipeFragment extends Fragment implements DialogIng.OnInputSelected{
    private AutoCompleteTextView textIn;
    private EditText quantity, prep, name, category, time;
    private Button buttonAdd, buttonSave;
    private String qua, ing, type;
    private IngredientInfo ingr;
    private String[] types = {"gr","ml","psc","tbsp"};
    private String[] myListIng;
    private TextView mInputDisplay;
    private Repository repo = new Repository();

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
        View v = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        Spinner spinner =(Spinner) v.findViewById(R.id.spinner2);

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
        ScrollView mainScroll = (ScrollView) v.findViewById(R.id.mainScroll);
        textIn = (AutoCompleteTextView) v.findViewById(R.id.textin);
        repo.getAllIngredients(new Repository.OnSearchAllIngredients() {
            String[] emptyList = new String[]{};
            @Override
            public void onIngredientsFound(List <Ingredient> matches) {
                myListIng = new String[matches.size()];
                for(int i=0; i<matches.size() ; ++i){
                    myListIng[i] = matches.get(i).getName();
                    Log.d("myListIng[i]","myListIng ["+i+"]"+" "+myListIng[i]);
                }
                textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, myListIng));
            }
            @Override
            public void onNoIngredientsFound(String message) {
                textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, emptyList));
            }
            @Override
            public void onExceptionOccurred(Exception e) {
                textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, emptyList));
            }
        });


        quantity = (EditText) v.findViewById(R.id.quantity);
        name = (EditText) v.findViewById(R.id.name);
        category = (EditText) v.findViewById(R.id.category);
        time = (EditText) v.findViewById(R.id.time);
        prep = (EditText) v.findViewById(R.id.prep);
        buttonAdd = (Button) v.findViewById(R.id.add);
        buttonSave = (Button) v.findViewById(R.id.saveBtn);
        LinearLayout container2 = (LinearLayout) v.findViewById(R.id.container);
        List<IngredientInfo> allIngredients = new ArrayList<>();


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ing = textIn.getText().toString().trim();
                qua = quantity.getText().toString().trim();
                //check if the ingredient exist
                repo.isExistIngredient(ing, new Repository.OnAddNewIngredientListener() {
                    @Override
                    public void onSuccess(String message) {
                        if(message=="false"){
                            // Create and show the dialog.
                            DialogIng dialog = new DialogIng();
                            dialog.setTargetFragment(AddRecipeFragment.this, 1);
                            dialog.show(getFragmentManager(), "DialogIng");
                            Log.d("O","_________  "+dialog.getNameIng());
                            addToArray(dialog.getNameIng());
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        final View addView = layoutInflater.inflate(R.layout.row, null);
                        TextView textOut = (TextView) addView.findViewById(R.id.textout);
                        TextView textOut2 = (TextView) addView.findViewById(R.id.textout2);
                        TextView textOutType = (TextView) addView.findViewById(R.id.textout3);
                        textOut.setText(textIn.getText().toString());
                        textOut2.setText(quantity.getText().toString());
                        textOutType.setText(type);

                        ingr = new IngredientInfo(ing, Double.parseDouble(qua),type);
                        allIngredients.add(ingr);
                        textIn.setText("");
                        quantity.setText("");
                        Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                        buttonRemove.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
                                for (int i = 0; i < allIngredients.size(); ++i) {
                                    if (allIngredients.get(i).getName().equals(textOut.getText().toString()))
                                        allIngredients.remove(i);
                                }
                            }
                        });

                        container2.addView(addView);
                    }
                });

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LLLLLLLLLLLLL", "allIngredients=====" + allIngredients);
                String Name = name.getText().toString().trim();
                String Time = time.getText().toString().trim();
                String Category = category.getText().toString().trim();
                String Prep = prep.getText().toString().trim();
                Recipe recipe = new Recipe(Name,Category,Time,allIngredients,Prep);
                repo.saveNewRecipe(recipe, new Repository.OnAddNewRecipeListener() {
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
            }
        });


//        EditText nameRecipe= (EditText) v.findViewById(R.id.nameRecipe);
//        EditText category= (EditText)v.findViewById(R.id.category);
//        EditText preparationTime= (EditText)v.findViewById(R.id.preparationTime);
//        EditText ingredients= (EditText)v.findViewById(R.id.ingredients);
//        EditText preparationMethod= (EditText) v.findViewById(R.id.preparationMethod);
//        Button btnAdd= (Button) v.findViewById(R.id.addRecipeBtn);
//
//        btnAdd.setOnClickListener((vi)->{
//            String NameRecipe= nameRecipe.getText().toString();
//            String Category= category.getText().toString();
//            String PreparationTime= preparationTime.getText().toString();
//            String PreparationMethod= preparationMethod.getText().toString();
//            String Ingredients= ingredients.getText().toString();
//            Log.d("TAG","Ingredients====="+Ingredients);
//            List<IngredientInfo> allIngredients;
//
//            String[] str = Ingredients.split("\r\n");
//            Log.d("","Arrays.toString(str)========"+Arrays.toString(str));
//
//            Log.d("TAG","str====="+str);
//            IngredientInfo ing;
//            allIngredients = new ArrayList<>();
//            for (String s: str) {
//                Log.d("TAG", "s=====" + s);
//                String[] ing1 = s.split(" ");
//
//                Log.d("TAG", "ing1=====" + ing1);
//                ing = new IngredientInfo(ing1[0], Double.parseDouble(ing1[1]));
//                Log.d("TAG", "ing====" + ing);
//                allIngredients.add(ing);
//            }
//
//            Recipe recipe = new Recipe(NameRecipe,Category,PreparationTime,allIngredients,PreparationMethod);
//            repo.saveNewRecipe(recipe, new RecipeRepository.OnAddNewRecipeListener() {
//                @Override
//                public void onSuccess(String message) {
//                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
//                    Log.d("saveNewRecipe::Succeed",message);
//                }
//
//                @Override
//                public void onFailure(Exception e) {
//                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                    Log.d("saveNewRecipe::Failure",e.getLocalizedMessage());
//                }
//            });
//            Intent intent=new Intent();
//            intent.setClass(getActivity(), MainActivity.class);
//            getActivity().startActivity(intent);
//        });

        return v;

    }

    public void addToArray(String newIng){
        if(myListIng!=null) {
            myListIng = Arrays.copyOf(myListIng, myListIng.length + 1);
            myListIng[myListIng.length - 1] = newIng;
            textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, myListIng));
        }
    }

    @Override
    public void sendInput(String input) {
        Log.d("DDDialog", "sendInput: found incoming input: " + input);

        mInputDisplay.setText(input);
    }
}
