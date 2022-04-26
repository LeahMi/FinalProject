package com.dvora.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.entities.Ingredient;
import com.dvora.finalproject.entities.IngredientInfo;
import com.dvora.finalproject.entities.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dvora.finalproject.RecipeRepository.INGREDIENTS_PATH;


public class AddRecipeFragment extends Fragment {
    private AutoCompleteTextView textIn;
    private EditText quantity;
    private EditText prep;
    private EditText name;
    private EditText category;
    private EditText time;
    private Button buttonAdd;
    private Button buttonSave;
    private String qua;
    private String ing;
    private IngredientInfo ingr;
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
//        View v=inflater.inflate(R.layout.fragment_add_recipe, container, false);
        View v = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        // ScrollView scrollView = (ScrollView)v.findViewById(R.id.scrollView);
        ScrollView mainScroll = (ScrollView) v.findViewById(R.id.mainScroll);
        textIn = (AutoCompleteTextView) v.findViewById(R.id.textin);

        repo.getAllIngredients(new RecipeRepository.OnSearchAllIngredients() {
            String[] emptyList = new String[]{};
            @Override
            public void onIngredientsFound(List <Ingredient> matches) {
                String[] myListIng = new String[matches.size()];
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
                final View addView = layoutInflater.inflate(R.layout.row, null);
                TextView textOut = (TextView) addView.findViewById(R.id.textout);
                TextView textOut2 = (TextView) addView.findViewById(R.id.textout2);
                textOut.setText(textIn.getText().toString());
                textOut2.setText(quantity.getText().toString());
                ing = textIn.getText().toString().trim();
                qua = quantity.getText().toString().trim();
                ingr = new IngredientInfo(ing, Double.parseDouble(qua),"type");
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

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LLLLLLLLLLLLL", "allIngredients=====" + allIngredients);
                String Name = name.getText().toString().trim();
                String Time = time.getText().toString().trim();
                String Category = category.getText().toString().trim();
                String Prep = prep.getText().toString().trim();
                Recipe recipe = new Recipe(Name,Category,Time,allIngredients,Prep);
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
}
//package com.dvora.finalproject;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.dvora.finalproject.activities.MainActivity;
//import com.dvora.finalproject.entities.IngredientInfo;
//import com.dvora.finalproject.entities.Recipe;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.dvora.finalproject.RecipeRepository.INGREDIENTS_PATH;
//
//
//public class AddRecipeFragment extends Fragment {
//    private AutoCompleteTextView textIn;
//    private EditText quantity;
//    private EditText prep;
//    private EditText name;
//    private EditText category;
//    private EditText time;
//    private Button buttonAdd;
//    private Button buttonSave;
//    private String qua;
//    private String ing;
//    private IngredientInfo ingr;
//    //private Button btnAdd;
////    private EditText nameRecipe;
////    private EditText category;
////    private EditText preparationTime;
////    private EditText ingredients;
////    private EditText preparationMethod;
//    // private String nameIngredient;
//    // private double quantityIngredient;
//
//    private RecipeRepository repo = new RecipeRepository();
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        IngredientInfo ing = new IngredientInfo("Pepper",5.5);
////        List<IngredientInfo> allIngredients = new ArrayList<>();
////        allIngredients.add(ing);
////        Recipe recipe = new Recipe("Chicken","Chicken","200",allIngredients,"Deep cook");
////        repo.saveNewRecipe(recipe, new RecipeRepository.OnAddNewRecipeListener() {
////            @Override
////            public void onSuccess(String message) {
////                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
////                Log.d("saveNewRecipe::Succeed",message);
////            }
////
////            @Override
////            public void onFailure(Exception e) {
////                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
////                Log.d("saveNewRecipe::Failure",e.getLocalizedMessage());
////            }
////        });
////        repo.getRecipesForIngredient("Pepper", new RecipeRepository.OnSearchRecipesByIngredient() {
////            @Override
////            public void onRecipesFound(List<Recipe> matches) {
////                Toast.makeText(getContext(),matches.size() + " Recipes Found!",Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onNoRecipesFound(String message) {
////                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onExceptionOcured(Exception e) {
////                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
////            }
////        });
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
////        View v=inflater.inflate(R.layout.fragment_add_recipe, container, false);
//        View v = inflater.inflate(R.layout.fragment_add_recipe, container, false);
//        // ScrollView scrollView = (ScrollView)v.findViewById(R.id.scrollView);
//        ScrollView mainScroll = (ScrollView) v.findViewById(R.id.mainScroll);
//        textIn = (AutoCompleteTextView) v.findViewById(R.id.textin);
//
//        repo.getAllIngredients(new RecipeRepository.OnSearchAllIngredients() {
//            String[] emptyList = new String[]{};
//            @Override
//            public void onIngredientsFound(String[] matches) {
//                String[] myListIng = new String[matches.length];
//                for(int i=0; i<matches.length ; ++i){
//                    myListIng[i] = matches[i];
//                    Log.d("myListIng[i]","myListIng ["+i+"]"+" "+myListIng[i]);
//                }
//                textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, myListIng));
//            }
//            @Override
//            public void onNoIngredientsFound(String message) {
//                textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, emptyList));
//            }
//            @Override
//            public void onExceptionOccurred(Exception e) {
//                textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, emptyList));
//            }
//        });
//
//
//        quantity = (EditText) v.findViewById(R.id.quantity);
//        name = (EditText) v.findViewById(R.id.name);
//        category = (EditText) v.findViewById(R.id.category);
//        time = (EditText) v.findViewById(R.id.time);
//        prep = (EditText) v.findViewById(R.id.prep);
//        buttonAdd = (Button) v.findViewById(R.id.add);
//        buttonSave = (Button) v.findViewById(R.id.saveBtn);
//        LinearLayout container2 = (LinearLayout) v.findViewById(R.id.container);
//        List<IngredientInfo> allIngredients = new ArrayList<>();
//
//
//        buttonAdd.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                LayoutInflater layoutInflater =
//                        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                final View addView = layoutInflater.inflate(R.layout.row, null);
//                TextView textOut = (TextView) addView.findViewById(R.id.textout);
//                TextView textOut2 = (TextView) addView.findViewById(R.id.textout2);
//                textOut.setText(textIn.getText().toString());
//                textOut2.setText(quantity.getText().toString());
//                ing = textIn.getText().toString().trim();
//                qua = quantity.getText().toString().trim();
//                ingr = new IngredientInfo(ing, Double.parseDouble(qua));
//                allIngredients.add(ingr);
//                textIn.setText("");
//                quantity.setText("");
//                Button buttonRemove = (Button) addView.findViewById(R.id.remove);
//                buttonRemove.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        ((LinearLayout) addView.getParent()).removeView(addView);
//                        for (int i = 0; i < allIngredients.size(); ++i) {
//                            if (allIngredients.get(i).getName().equals(textOut.getText().toString()))
//                                allIngredients.remove(i);
//                        }
//                    }
//                });
//
//                container2.addView(addView);
//            }
//        });
//
//        buttonSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("LLLLLLLLLLLLL", "allIngredients=====" + allIngredients);
//                String Name = name.getText().toString().trim();
//                String Time = time.getText().toString().trim();
//                String Category = category.getText().toString().trim();
//                String Prep = prep.getText().toString().trim();
//                Recipe recipe = new Recipe(Name,Category,Time,allIngredients,Prep);
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
//            }
//        });
//
//
////        EditText nameRecipe= (EditText) v.findViewById(R.id.nameRecipe);
////        EditText category= (EditText)v.findViewById(R.id.category);
////        EditText preparationTime= (EditText)v.findViewById(R.id.preparationTime);
////        EditText ingredients= (EditText)v.findViewById(R.id.ingredients);
////        EditText preparationMethod= (EditText) v.findViewById(R.id.preparationMethod);
////        Button btnAdd= (Button) v.findViewById(R.id.addRecipeBtn);
////
////        btnAdd.setOnClickListener((vi)->{
////            String NameRecipe= nameRecipe.getText().toString();
////            String Category= category.getText().toString();
////            String PreparationTime= preparationTime.getText().toString();
////            String PreparationMethod= preparationMethod.getText().toString();
////            String Ingredients= ingredients.getText().toString();
////            Log.d("TAG","Ingredients====="+Ingredients);
////            List<IngredientInfo> allIngredients;
////
////            String[] str = Ingredients.split("\r\n");
////            Log.d("","Arrays.toString(str)========"+Arrays.toString(str));
////
////            Log.d("TAG","str====="+str);
////            IngredientInfo ing;
////            allIngredients = new ArrayList<>();
////            for (String s: str) {
////                Log.d("TAG", "s=====" + s);
////                String[] ing1 = s.split(" ");
////
////                Log.d("TAG", "ing1=====" + ing1);
////                ing = new IngredientInfo(ing1[0], Double.parseDouble(ing1[1]));
////                Log.d("TAG", "ing====" + ing);
////                allIngredients.add(ing);
////            }
////
////            Recipe recipe = new Recipe(NameRecipe,Category,PreparationTime,allIngredients,PreparationMethod);
////            repo.saveNewRecipe(recipe, new RecipeRepository.OnAddNewRecipeListener() {
////                @Override
////                public void onSuccess(String message) {
////                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
////                    Log.d("saveNewRecipe::Succeed",message);
////                }
////
////                @Override
////                public void onFailure(Exception e) {
////                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
////                    Log.d("saveNewRecipe::Failure",e.getLocalizedMessage());
////                }
////            });
////            Intent intent=new Intent();
////            intent.setClass(getActivity(), MainActivity.class);
////            getActivity().startActivity(intent);
////        });
//
//        return v;
//
//    }
//}