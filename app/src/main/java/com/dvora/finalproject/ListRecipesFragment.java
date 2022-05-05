package com.dvora.finalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.dvora.finalproject.entities.Ingredient;
import com.dvora.finalproject.entities.IngredientInfo;
import com.dvora.finalproject.entities.Recipe;
import com.dvora.finalproject.fragments.ItemDetailsFragment;

import java.util.ArrayList;
import java.util.List;


public class ListRecipesFragment extends Fragment {
    private ListView list;
    private ImageButton btnAdd;
    private SearchView searchView;
    private List<IngredientInfo> listIngredients;
    private int count = 0;
    private List<String> listInventory =new ArrayList<String>();
    private List<Double> listPercent =new ArrayList<Double>();

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
            public void onRecipesFound(List<Recipe> matches,List<Double> listPercent) {
                list= v.findViewById(R.id.mainlistfragment_listv);
                searchView= v.findViewById(R.id.search_bar);
                //listPercent = getPercent(matches);
                Log.d("1","1");
                Log.d("ListPer_ ","ListPer_ "+listPercent);
                RecipeAdapter adapter = new RecipeAdapter(matches, listPercent, getContext(), new ICallbackAdapter() {

                    @Override
                    public void onClickItem(Recipe recipe) {
                        openDetailsFragment(recipe);
                    }
                });
                list.setAdapter(adapter);
                //list.setAdapter(arrayAdapter);
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
//    private List<Integer> getPercent(List<Recipe> recipes){
//        Log.d("4","4");
//        repo.getAllIngredients(new Repository.OnSearchAllIngredients() {
//            @Override
//            public void onIngredientsFound(List<Ingredient> matches) {
//                Log.d("5","5");
//                int numOfIngredients = 0;
//                for(Ingredient ingInventory :matches){
//                    Log.d("6","6");
//                    listInventory.add(ingInventory.getName());
//                    Log.d("7","7");
//                }
//                Log.d("listInventory","listInventory size___ "+listInventory.size() );
//                Log.d("listInventory","listInventory___ "+listInventory );
//                for (Recipe recipe:recipes) {
//                    Log.d("8","8");
//                    count = 0;
//                    listIngredients = recipe.getIngredients();
//                    numOfIngredients = listIngredients.size();
//                    for (IngredientInfo ing :listIngredients) {
//                        Log.d("9","9");
//                        if(listInventory.contains(ing.getName()))
//                            Log.d("10","10");
//                            count++;
//                    }
//                    if(count!=0) {
//                        Log.d("11","11");
//                        int per =(count/numOfIngredients)*100;
//                        Log.d("pppppper","perrrrrrrrrrrr___ "+per );
//                        listPercent.add(per);
//                    }
//                    else
//                        listPercent.add(0);
//                }
//
//            }
//
//            @Override
//            public void onNoIngredientsFound(String message) {
//                for (Recipe recipe:recipes) {
//                    listPercent.add(0);
//                }
//            }
//
//            @Override
//            public void onExceptionOccurred(Exception e) {
//
//            }
//        });
//        return listPercent;
//    }

}