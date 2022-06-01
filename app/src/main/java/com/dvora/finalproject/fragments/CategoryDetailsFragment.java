package com.dvora.finalproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.dvora.finalproject.ICallbackAdapter;
import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
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
    private Repository repo= new Repository();
    private List<Recipe> recipesList = new ArrayList<>();


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
        if(getArguments()!=null) {
            Bundle bundle = getArguments();
            category = (Category) bundle.getSerializable(CATEGORY_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_category_details, container, false);
        btnAdd = (ImageButton) v.findViewById(R.id.btn_add_rec);
        name = (TextView) v.findViewById(R.id.name_category_tv);
        name.setText(category.getName());
        repo.getAllRecipes(new Repository.OnSearchAllRecipes() {
            @Override
            public void onRecipesFound(List<Recipe> matches) {
                rvRecipes = v.findViewById(R.id.rv_recipes);
                searchView= v.findViewById(R.id.search_bar_category);
                for (Recipe recipe:matches){
                    if(recipe.getCategory().equals(category.getName()))
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
//                list= v.findViewById(R.id.list_view);
//                searchView= v.findViewById(R.id.search_bar_category);
//                for (Recipe recipe:matches){
//                    if(recipe.getCategory().equals(category.getName()))
//                        recipesList.add(recipe);
//                }
//                RecipeAdapter adapter = new RecipeAdapter(recipesList, getContext(), new ICallbackAdapter() {
//
//                    @Override
//                    public void onClickItem(Recipe recipe) {
//                        openDetailsFragment(recipe);
//                    }
//                });
//                list.setAdapter(adapter);
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
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExceptionOccurred(Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
        tran.replace(R.id.fragment, frag);
        tran.commit();
    }
    private void openDetailsFragment(Recipe recipe) {
        showFragment(ItemDetailsFragment.newInstance(recipe));
    }
    private void openDetailsFragment1(Category category) {
        showFragment(AddRecipeFragment.newInstance(category));
    }

}