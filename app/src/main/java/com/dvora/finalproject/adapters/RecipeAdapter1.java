package com.dvora.finalproject.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dvora.finalproject.ICallbackAdapter;
import com.dvora.finalproject.R;
import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter1 extends BaseAdapter implements Filterable {

    private final Context context;
    private final List<Recipe> data;
    private final List<Recipe> exampleList;
    private final LayoutInflater inflater;
    private final ICallbackAdapter iCallbackAdapter;
    private List<Recipe> filteredList = new ArrayList<>();

    public RecipeAdapter1(List<Recipe> data, Context context, ICallbackAdapter callbackAdapter) {
        this.context =context;
        this.data = data;
        this.exampleList = new ArrayList<>(data);
        this.inflater = LayoutInflater.from(context);
        this.iCallbackAdapter = callbackAdapter;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;

    }

    @Override
    public View getView(int pos /* row index*/, View ROW /* reusable ROW */, ViewGroup viewGroup) {
        if (ROW == null) {
            ROW = inflater.inflate(R.layout.main_list_row, null);
            Log.d("TAG", "creating new row ROW");
        } else {
            Log.d("TAG", "reusing old row ROW");
        }

        final Recipe recipeRow = data.get(pos);
        TextView tv = ROW.findViewById(R.id.mainlistrow_text_v);
        TextView tv2 = ROW.findViewById(R.id.mainlistrow_text_v2);
        TextView tv3 = ROW.findViewById(R.id.mainlistrow_text_v3);
        ImageView imageViewRecipe = ROW.findViewById(R.id.img_recipe);
        ImageView imageViewFood = ROW.findViewById(R.id.img_food);
        tv.setText(recipeRow.getNameRecipe());
        tv2.setText(recipeRow.getPreparationTime());
        tv3.setText(recipeRow.getPercentIng().toString() + " %");
//        if(recipeRow.getPercentIng()!=100.0) {
//            tv3.setTextColor(Color.RED);
//        } else{
//            tv3.setTextColor(Color.GREEN);
//        }
        if(recipeRow.getPercentIng()!=100.0) {
            imageViewFood.setImageResource(R.drawable.food_red);
        } else{
            imageViewFood.setImageResource(R.drawable.food_green);
        }
        imageViewRecipe.setImageResource(R.drawable.image_recipe);
        if (recipeRow.getImgUrl().equals("null")) {
            imageViewRecipe.setImageResource(R.drawable.image_recipe);
        } else {
            Glide.with(context)
                    .load(data.get(pos).getImgUrl())
                    .into(imageViewRecipe);
        }
        ROW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("asds", "ROW");

                if (iCallbackAdapter != null) {
                    iCallbackAdapter.onClickItem(recipeRow);
                }
            }
        });
        return ROW;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private final Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                for (Recipe item : exampleList){
                    if(isFilter(item)) {
                        filteredList.add(item);
                    }
                }
//                filteredList.addAll(exampleList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Recipe item : exampleList) {
                    if (item.getNameRecipe().toLowerCase().contains(filterPattern) ||
                            item.getCategory().toLowerCase().contains(filterPattern) ||
                            item.getPercentIng().toString().toLowerCase().contains(filterPattern) ||
                            item.getLevel().toLowerCase().contains(filterPattern) ||
                            item.getPreparationTime().toLowerCase().contains(filterPattern) ||
                            item.getIngredientInfoToString().toLowerCase().contains(filterPattern)) {
                        if(isFilter(item))
                            filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    public boolean isFilter(Recipe recipe){
        Log.e("RAF isClock",""+isClock(recipe));
        Log.e("RAF isLevel",""+isLevel(recipe));
        Log.e("RAF isPercent",""+isPercent(recipe));
        Log.v("isF","isF "+ isFavorite(recipe));
        return (isClock(recipe) && isLevel(recipe) && isPercent(recipe)) || isPercent(recipe)|| isFavorite(recipe)|| MainActivity.sort.equals("null");
    }
    public boolean isClock(Recipe recipe){
        return MainActivity.sort.contains(recipe.getPreparationTime()) && _isClock(recipe);
    }
    private boolean _isClock(Recipe recipe){
        return MainActivity.sort.contains("שעה") || MainActivity.sort.contains("שעה+") || MainActivity.sort.contains("בחר זמן") || MainActivity.sort.contains("דק'");
    }

    public boolean isLevel(Recipe recipe){
        return MainActivity.sort.contains(recipe.getLevel()) || MainActivity.sort.contains("בחר דרגה");
    }

    public boolean isPercent(Recipe recipe){
        if(!MainActivity.sort.contains("100%") || MainActivity.sort.contains("בחר כמות מוצרים")){
            return true;
        }
        if(recipe.getPercentIng()<100){
            return MainActivity.sort.contains("פחות מ 100%");
        }
        else{
            return !MainActivity.sort.contains("פחות מ 100%");
        }
    }
    public boolean isFavorite(Recipe recipe) {
        boolean b =MainActivity.sort.contains(recipe.getCategory()) ;
        boolean b1 = MainActivity.sort.contains("noFavorite");
        Log.v("b","b "+b);
        Log.v("b1","b1 "+b1);
        return MainActivity.sort.contains(recipe.getCategory()) || MainActivity.sort.contains("noFavorite");
    }

}