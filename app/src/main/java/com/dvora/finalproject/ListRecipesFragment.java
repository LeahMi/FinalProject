package com.dvora.finalproject;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.dvora.finalproject.adapters.RecipeAdapter;
import com.dvora.finalproject.adapters.RecipeAdapter1;
import com.dvora.finalproject.entities.Category;
import com.dvora.finalproject.entities.Recipe;
import com.dvora.finalproject.fragments.AddRecipeFragment;
import com.dvora.finalproject.fragments.CategoryDetailsFragment;
import com.dvora.finalproject.fragments.ItemDetailsFragment;
import com.dvora.finalproject.fragments.SplashFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ListRecipesFragment extends Fragment {
    private ListView list;
    private ImageButton btnAdd;
    private SearchView searchView;
    private Category category;
    public final static String CATEGORY_KEY = "CATEGORY_KEY";

    Dialog d;
    MultiSpinner timeSp, levelSp, numOfRecipesSp;
    ArrayList<String> timeList = new ArrayList<>(), levelList = new ArrayList<>(), numOfRecipesList = new ArrayList<>();
    Button okBtn;
    private ImageButton sortBtn;

    private Repository repo = new Repository();

    public static ListRecipesFragment newInstance(Category category) {
        ListRecipesFragment fragment = new ListRecipesFragment();
        Bundle args = new Bundle();
        args.putSerializable(CATEGORY_KEY, category);
        fragment.setArguments(args);
        return fragment;
    }

    public void createSortDialog() {
        d = new Dialog(getContext());
        d.setContentView(R.layout.sort_layout); // *
        //d.setTitle("הרשמה");
        d.setCancelable(true);
        timeSp = (MultiSpinner) d.findViewById(R.id.time_sp);
        levelSp = (MultiSpinner) d.findViewById(R.id.level_sp);
        numOfRecipesSp = (MultiSpinner) d.findViewById(R.id.num_of_recipes_sp);
        timeList.clear();
        timeList.add("10 דק'");
        timeList.add("15 דק'");
        timeList.add("30 דק'");
        timeList.add("45 דק'");
        timeList.add("שעה");
        timeList.add("שעה+");
        timeSp.setItems(timeList, "בחר זמן", new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

            }
        });
        levelList.clear();
        levelList.add("קל");
        levelList.add("בינוני");
        levelList.add("קשה");
        levelSp.setItems(levelList, "בחר דרגה", new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

            }
        });
        numOfRecipesList.clear();
        numOfRecipesList.add("100%");
        numOfRecipesList.add("פחות מ 100%");
        numOfRecipesSp.setItems(numOfRecipesList, "בחר כמות מוצרים", new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

            }
        });
        okBtn = d.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashFragment.sort = "";
                SplashFragment.sort += " " + timeSp.getSpinnerText();
                SplashFragment.sort += " " + levelSp.getSpinnerText();
                SplashFragment.sort += " " + numOfRecipesSp.getSpinnerText();
                Toast.makeText(getContext(),"" +SplashFragment.sort,Toast.LENGTH_LONG).show();
                Log.e("CDF recipe filter:","" + SplashFragment.sort);
                createNewListBySort(SplashFragment.sort);

                d.dismiss();
            }
        });
        d.show();
    }

    public boolean isFilter(Recipe recipe){
        Log.e("CDF name",""+recipe.getNameRecipe());
        Log.e("CDF isClock",""+isClock(recipe));
        Log.e("CDF isLevel",""+isLevel(recipe));
        Log.e("CDF isPercent",""+isPercent(recipe));
        return (isClock(recipe) && isLevel(recipe) && isPercent(recipe)) || SplashFragment.sort.equals("null");
    }
    public boolean isClock(Recipe recipe){
        return SplashFragment.sort.contains(recipe.getPreparationTime()) || _isClock(recipe);
    }
    private boolean _isClock(Recipe recipe){
        return SplashFragment.sort.contains("שעה") || SplashFragment.sort.contains("שעה+") || SplashFragment.sort.contains("בחר זמן") || SplashFragment.sort.contains("דק'");
    }

    public boolean isLevel(Recipe recipe){
        return SplashFragment.sort.contains(recipe.getLevel()) || SplashFragment.sort.contains("בחר דרגה");
    }

    public boolean isPercent(Recipe recipe){
        if(!SplashFragment.sort.contains("100%") || SplashFragment.sort.contains("בחר כמות מוצרים")){
            return true;
        }
        if(recipe.getPercentIng()<100){
            return SplashFragment.sort.contains("פחות מ 100%");
        }
        else{
            return !SplashFragment.sort.contains("פחות מ 100%");
        }
    }
    public void createNewListBySort(String spinnerText) {
        repo.getAllRecipes(new Repository.OnSearchAllRecipes() {
            @Override
            public void onRecipesFound(List<Recipe> matches) {
                ArrayList<Recipe> recipes = new ArrayList<Recipe>();
                for(Recipe recipe: matches) {
                    if(isFilter(recipe) && recipe.getCategory().equals(category.getName()))
                        recipes.add(recipe);
                    else if(isFilter(recipe) && category.getName().equals("כל המתכונים"))
                        recipes.add(recipe);
                }
                RecipeAdapter1 adapter = new RecipeAdapter1(recipes, getContext(), new ICallbackAdapter() {
                    @Override
                    public void onClickItem(Recipe recipe) {
                        openDetailsFragment(recipe);
                    }
                });
                list.setAdapter(adapter);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        adapter.getFilter().filter(query);
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {

                        adapter.getFilter().filter(newText);
                        return false;
                    }
                });
            }
            public void onNoRecipesFound(String message){
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExceptionOccurred(Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            category = (Category) bundle.getSerializable(CATEGORY_KEY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_list_recipes, container, false);
        sortBtn = v.findViewById(R.id.sort_btn1);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSortDialog();
            }
        });
        repo.getAllRecipes(new Repository.OnSearchAllRecipes() {

            @Override
            public void onRecipesFound(List<Recipe> matches) {
                list= v.findViewById(R.id.mainlistfragment_listv);
                searchView= v.findViewById(R.id.search_bar);

                ArrayList<Recipe> recipes = new ArrayList<>();
                for(Recipe recipe : matches)
                {
                    if(isFilter(recipe) && recipe.getCategory().equals(category.getName()))
                        recipes.add(recipe);
                    else if(isFilter(recipe) && category.getName().equals("כל המתכונים"))
                        recipes.add(recipe);
                }

                RecipeAdapter1 adapter = new RecipeAdapter1(recipes, getContext(), new ICallbackAdapter() {

                    @Override
                    public void onClickItem(Recipe recipe) {
                        openDetailsFragment(recipe);
                    }
                });
                list.setAdapter(adapter);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        adapter.getFilter().filter(query);
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {

                        adapter.getFilter().filter(newText);
                        return false;
                    }
                });
            }
            public void onNoRecipesFound(String message){
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExceptionOccurred(Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        btnAdd = (ImageButton) v.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(AddRecipeFragment.newInstance(category));
            }
        });


        return v;
    }
    public void showFragment(Fragment frag) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.replace(R.id.fragment, frag).addToBackStack(null);
        tran.commit();
    }

    private void openDetailsFragment(Recipe recipe) {
        showFragment(ItemDetailsFragment.newInstance(recipe));
    }


}