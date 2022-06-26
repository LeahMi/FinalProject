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
import android.widget.ImageButton;


import com.dvora.finalproject.FirebaseManager;
import com.dvora.finalproject.ICallBackAdapterCategory;
import com.dvora.finalproject.ListRecipesFragment;
import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.adapters.CategoryAdapter;
import com.dvora.finalproject.entities.Category;


import java.util.ArrayList;
import java.util.List;


public class CategoriesFragment extends BaseFragment {

    private ImageButton btnAdd;
    private RecyclerView rvCategories;
    private Repository repo = new Repository();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_categories, container, false);
        MainActivity.sort="null";
        FirebaseManager.setAllergens();
        repo.getAllCategories(new Repository.OnSearchAllCategories() {
            @Override
            public void onCategoriesFound(List<Category> matches) {
                rvCategories = v.findViewById(R.id.categories_list);
                List<Category> matches1 = new ArrayList<>();
                int numRecipes=0;
                for (int i=0;i<matches.size();++i) {
                    matches1.add(matches.get(i));
                    numRecipes+=matches.get(i).getNumOfRecipes();
                }
                matches1.add(new Category());
                matches1.get(matches1.size()-1).setNumOfRecipes(numRecipes);
                CategoryAdapter adapter = new CategoryAdapter(getContext(), matches1, new ICallBackAdapterCategory() {
                    @Override
                    public void onClickItem(Category category) {
                        openDetailsFragment(category);
                    }
                });
                rvCategories.setAdapter(adapter);
//                rvCategories.setItemAnimator(new SlideInUpAnimator());
                rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onNoCategoriesFound(String message) {

            }

            @Override
            public void onExceptionOccurred(Exception e) {

            }
        });

        btnAdd = (ImageButton) v.findViewById(R.id.btn_add_category);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"message",Toast.LENGTH_SHORT).show();
                mListener.showFragment(R.id.nav_add_category);
            }
        });
        return v;
    }


    private void openDetailsFragment(Category category) {
        mListener.showFragment(R.id.listRecipesFragment2,ListRecipesFragment.newInstance(category));
    }

}