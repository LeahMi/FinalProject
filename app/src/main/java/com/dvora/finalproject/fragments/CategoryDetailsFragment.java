package com.dvora.finalproject.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.dvora.finalproject.ICallbackAdapter;
import com.dvora.finalproject.MultiSpinner;
import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.adapters.RecipeAdapter;
import com.dvora.finalproject.entities.Category;
import com.dvora.finalproject.entities.Recipe;

import java.util.ArrayList;
import java.util.List;


public class CategoryDetailsFragment extends Fragment {

    private RecyclerView rvRecipes;
    public final static String CATEGORY_KEY = "CATEGORY_KEY";
    private Category category;
    private ImageButton btnAdd;
    private TextView name;
    private ListView list;
    private SearchView searchView;
    private Repository repo = new Repository();
    private List<Recipe> recipesList = new ArrayList<>();

    Dialog d;
    MultiSpinner timeSp, levelSp, numOfRecipesSp;
    ArrayList<String> timeList = new ArrayList<>(), levelList = new ArrayList<>(), numOfRecipesList = new ArrayList<>();
    Button okBtn;
    ImageButton sortBtn;

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
                SplashFragment.sort += "" + timeSp.getSpinnerText();
                SplashFragment.sort += "" + levelSp.getSpinnerText();
                SplashFragment.sort += "" + numOfRecipesSp.getSpinnerText();
                Toast.makeText(getContext(),"" +SplashFragment.sort,Toast.LENGTH_LONG).show();
                createNewListBySort(SplashFragment.sort);

                d.dismiss();
            }
        });
        d.show();
    }

    public void createNewListBySort(String spinnerText) {
        repo.getAllRecipes(new Repository.OnSearchAllRecipes() {
            @Override
            public void onRecipesFound(List<Recipe> matches) {
                for (Recipe recipe : matches) {
                    if(spinnerText!="") {
                        if (recipe.getCategory().equals(category.getName())
                                && spinnerText.contains(recipe.getPreparationTime())
                                && spinnerText.contains(recipe.getLevel())
                                && spinnerText.contains(recipe.getPercentIng().toString())) {
                            recipesList.add(recipe);
                        }
                    }
                    else
                        recipesList.add(recipe);
                }
                RecipeAdapter adapter = new RecipeAdapter(getContext(), recipesList, new ICallbackAdapter() {
                    @Override
                    public void onClickItem(Recipe recipe) {
                        openDetailsFragment(recipe);
                    }
                });
                adapter.getFilter().filter(spinnerText);
                rvRecipes.setAdapter(adapter);
                rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

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
                adapter.getFilter().filter(spinnerText);
            }

            @Override
            public void onNoRecipesFound(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExceptionOccurred(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static CategoryDetailsFragment newInstance(Category category) {
        CategoryDetailsFragment fragment = new CategoryDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(CATEGORY_KEY, category);
        fragment.setArguments(args);
        return fragment;

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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_category_details, container, false);
        sortBtn = v.findViewById(R.id.sort_btn);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSortDialog();
            }
        });
        btnAdd = (ImageButton) v.findViewById(R.id.btn_add_rec);
        name = (TextView) v.findViewById(R.id.name_category_tv);
        name.setText(category.getName());
        //((MainActivity) getActivity()).changeActionBarTitle(category.getName(),false);
        String s = "s";
        repo.getAllRecipes(new Repository.OnSearchAllRecipes() {
            @Override
            public void onRecipesFound(List<Recipe> matches) {
                rvRecipes = v.findViewById(R.id.rv_recipes);
                searchView = v.findViewById(R.id.search_bar_category);
                for (Recipe recipe : matches) {
                    if (recipe.getCategory().equals(category.getName()))
                        recipesList.add(recipe);
                }
                RecipeAdapter adapter = new RecipeAdapter(getContext(), recipesList, new ICallbackAdapter() {
                    @Override
                    public void onClickItem(Recipe recipe) {
                        openDetailsFragment(recipe);
                    }
                });
                rvRecipes.setAdapter(adapter);
                rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
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

            @Override
            public void onNoRecipesFound(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExceptionOccurred(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailsFragment1(category);
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

    private void openDetailsFragment1(Category category) {
        showFragment(AddRecipeFragment.newInstance(category));
    }

}