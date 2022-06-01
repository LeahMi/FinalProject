package com.dvora.finalproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dvora.finalproject.adapters.IngAdapter;
import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.entities.Ingredient;

import java.util.List;

public class InventoryFragment extends Fragment {
    private AutoCompleteTextView textIn;
    private ListView list;
    private ImageButton buttonAdd;
    private ImageButton ListShopping;
    private ArrayAdapter<String> arrayAdapter;
    String[] nameList = {};
    private SearchView searchViewInventory;

    private Repository repo = new Repository();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventory, container, false);
        searchViewInventory=v.findViewById(R.id.search_bar_inventory);
        repo.getAllIngredients(new Repository.OnSearchAllIngredients() {
            public void onIngredientsFound(List<Ingredient> matches){
                Toast.makeText(getContext(),matches.size() + " מרכיבים נמצאו",Toast.LENGTH_SHORT).show();
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
                searchViewInventory.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        adapter.getFilter().filter(s);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String a) {
                        adapter.getFilter().filter(a);
                        return false;
                    }
                });
            }


            @Override
            public void onNoIngredientsFound(String message) {
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onExceptionOccurred(Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

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