package com.dvora.finalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.dvora.finalproject.adapters.RecipeAdapter;
import com.dvora.finalproject.entities.Recipe;
import com.dvora.finalproject.fragments.AddRecipeFragment;
import com.dvora.finalproject.fragments.ItemDetailsFragment;

import java.util.List;


public class ListRecipesFragment extends Fragment {
    private ListView list;
    private ImageButton btnAdd;
    private SearchView searchView;


    private Repository repo = new Repository();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_list_recipes, container, false);

        repo.getAllRecipes(new Repository.OnSearchAllRecipes() {

            @Override
            public void onRecipesFound(List<Recipe> matches) {
                list= v.findViewById(R.id.mainlistfragment_listv);
                searchView= v.findViewById(R.id.search_bar);

//                RecipeAdapter adapter = new RecipeAdapter(matches, getContext(), new ICallbackAdapter() {
//
//                    @Override
//                    public void onClickItem(Recipe recipe) {
//                        openDetailsFragment(recipe);
//                    }
//                });
//                list.setAdapter(adapter);
//                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        adapter.getFilter().filter(query);
//                        return false;
//                    }
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//
//                        adapter.getFilter().filter(newText);
//                        return false;
//                    }
//                });
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
                showFragment(new AddRecipeFragment());
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

}