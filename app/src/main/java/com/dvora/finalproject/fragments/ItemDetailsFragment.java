package com.dvora.finalproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.dvora.finalproject.ListRecipesFragment;
import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.entities.Category;
import com.dvora.finalproject.entities.IngredientInfo;
import com.dvora.finalproject.entities.Recipe;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class ItemDetailsFragment extends Fragment implements View.OnClickListener {

    private Category c;
    public final static String RECIPE_KEY = "RECIPE_KEY";
    private Recipe recipe;
    private Button btn_updateInventory;
    private ImageView imageRecipe;
    private TextView nameRecipe;
    private ImageButton btn_back;
    private Repository repo = new Repository();

    public static ItemDetailsFragment newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putSerializable(RECIPE_KEY, recipe);
        ItemDetailsFragment fragment = new ItemDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        recipe = (Recipe) bundle.getSerializable(RECIPE_KEY);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_details, container, false);
        //((MainActivity)getActivity()).changeActionBarTitle(recipe.getCategory()  + ": " + recipe.getNameRecipe(),false);
        repo.getAllCategories(new Repository.OnSearchAllCategories() {
            @Override
            public void onCategoriesFound(List<Category> matches) {
                for (Category category:matches) {
                    if(category.getName().equals(recipe.getCategory()))
                        c = category;
                }
            }

            @Override
            public void onNoCategoriesFound(String message) {

            }

            @Override
            public void onExceptionOccurred(Exception e) {

            }
        });

        final EditText categoryEt = view.findViewById(R.id.edt_category);
        final EditText timeEt = view.findViewById(R.id.edt_time);
        final EditText prepEt = view.findViewById(R.id.edt_prep_method);
        final EditText IngEt = view.findViewById(R.id.edt_ing);
        final EditText levelEt = view.findViewById(R.id.level);
        levelEt.setText(recipe.getLevel());
        imageRecipe = view.findViewById(R.id.recipe_image);
        nameRecipe = view.findViewById(R.id.name_recipe);
        btn_updateInventory = view.findViewById(R.id.button);
        if(recipe.getPercentIng()!=100)
            btn_updateInventory.setVisibility(View.GONE);
        btn_updateInventory.setOnClickListener(this);

        btn_back = view.findViewById(R.id.btn_back);

        btn_back.setOnClickListener(this);
        categoryEt.setText(recipe.getCategory());
        nameRecipe.setText(recipe.getNameRecipe());
        timeEt.setText(recipe.getPreparationTime());
        prepEt.setText(recipe.getPreparationMethod());
        if (recipe.getImgUrl().equals("null")) {
            imageRecipe.setImageResource(R.drawable.image_recipe);
        } else {
            imageRecipe.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getContext())
                    .load(recipe.getImgUrl())
                    .into(imageRecipe);
        }
        List<IngredientInfo> list = recipe.getIngredients();
        String strList = "";
        for (int i = 0; i < list.size(); ++i) {
            if(i==list.size()-1)
                strList = strList + list.get(i).getQuantity()+" "+list.get(i).getType()+" "+list.get(i).getName();
            else
                strList = strList + list.get(i).getQuantity()+" "+list.get(i).getType()+" "+list.get(i).getName()+ '\n';
        }
        IngEt.setText(strList);

        return view;
    }

    public void showFragment(Fragment frag) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.replace(R.id.fragment, frag).addToBackStack(null);
        tran.commit();
    }
    private void openDetailsFragment(Category category) {
        showFragment(CategoryDetailsFragment.newInstance(category));
    }

    @Override
    public void onClick(View v) {
        if(v == btn_back){
            //getFragmentManager().popBackStackImmediate();
            openDetailsFragment(c);
            //showFragment(CategoryDetailsFragment.newInstance(new Category()));
        }
        if(v ==btn_updateInventory){
            repo.updateInventory(recipe, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getContext(), o.toString(), Toast.LENGTH_SHORT).show();
                        openDetailsFragment(c);
                    }
                });
        }

    }
}