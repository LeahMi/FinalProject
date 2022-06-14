package com.dvora.finalproject.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dvora.finalproject.ICallbackAdapter;
import com.dvora.finalproject.R;
import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> implements Filterable {
    private final Context context;
    private final List<Recipe> data;
    private final List<Recipe> exampleList;
    private final ICallbackAdapter iCallbackAdapter;
    private List<Recipe> filteredList = new ArrayList<>();

    public RecipeAdapter(Context context, List<Recipe> data, ICallbackAdapter callbackAdapter) {
        this.context = context;
        this.data = data;
        this.exampleList = new ArrayList<>(data);
        this.iCallbackAdapter = callbackAdapter;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.main_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recipeRow = data.get(position);
        holder.tv.setText(holder.recipeRow.getNameRecipe());
        holder.tv2.setText(holder.recipeRow.getPreparationTime());
        holder.tv3.setText(holder.recipeRow.getPercentIng().toString() + " %");
        if (holder.recipeRow.getImgUrl().equals("null")) {
            holder.imageViewRecipe.setImageResource(R.drawable.image_recipe);
        } else {
            Glide.with(context)
                    .load(data.get(position).getImgUrl())
                    .into(holder.imageViewRecipe);
        }
        if (holder.recipeRow.getPercentIng() == 100.0)
            holder.imgFood.setImageResource(R.drawable.food_green);
        else
            holder.imgFood.setImageResource(R.drawable.food_red);
        //holder.image.setImageResource(data.get(position).getImgUrl());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iCallbackAdapter != null) {
                    iCallbackAdapter.onClickItem(data.get(position));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Recipe recipeRow;
        TextView tv;
        TextView tv2;
        TextView tv3;
        View layout;
        ImageView imageViewRecipe, imgFood;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.mainlistrow_text_v);
            tv2 = itemView.findViewById(R.id.mainlistrow_text_v2);
            tv3 = itemView.findViewById(R.id.mainlistrow_text_v3);
            imageViewRecipe = itemView.findViewById(R.id.img_recipe);
            imgFood = itemView.findViewById(R.id.img_food);
            layout = itemView.findViewById(R.id.mainlistrow_lay);
        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private final Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList = new ArrayList<>();
            Log.e("exampleList", "" + exampleList.size());
            if (constraint == null || constraint.length() == 0) {
                // filteredList.addAll(exampleList);
                for (Recipe item : exampleList){
                    if(isFilter(item))
                        filteredList.add(item);
                }
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Recipe item : exampleList) {
                    if (
                            (item.getNameRecipe().toLowerCase().contains(filterPattern) ||
                            item.getCategory().toLowerCase().contains(filterPattern) ||
                            item.getPercentIng().toString().toLowerCase().contains(filterPattern) ||
                            item.getLevel().toLowerCase().contains(filterPattern) ||
                            item.getPreparationTime().toLowerCase().contains(filterPattern) ||
                            item.getIngredientInfoToString().toLowerCase().contains(filterPattern))
                    ) {
                        if(isFilter(item))
                            filteredList.add(item);
                    }
                }
            }
            Log.e("RAF recipe filter:","" + MainActivity.sort);
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

// && (MainActivity.sort.contains(item.getLevel())
//                                || MainActivity.sort.contains(item.getPercentIng().toString())
//                            || (MainActivity.sort.contains(item.getPreparationTime()))
//                            )
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
        return (isClock(recipe) && isLevel(recipe) && isPercent(recipe)) || MainActivity.sort.equals("null");
    }
    public boolean isClock(Recipe recipe){
        return MainActivity.sort.contains(recipe.getPreparationTime()) || _isClock(recipe);
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


}
