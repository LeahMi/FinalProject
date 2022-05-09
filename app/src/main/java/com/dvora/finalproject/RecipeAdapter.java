package com.dvora.finalproject;

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


import com.dvora.finalproject.entities.Ingredient;
import com.dvora.finalproject.entities.IngredientInfo;
import com.dvora.finalproject.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends BaseAdapter implements Filterable {

    private final List<Recipe> data;
    private final List<Recipe> exampleList;
    private final LayoutInflater inflater;
    private final ICallbackAdapter iCallbackAdapter;

    public RecipeAdapter(List<Recipe> data, Context context, ICallbackAdapter callbackAdapter) {
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
        ImageView imageViewProfile = ROW.findViewById(R.id.img_profile);
        tv.setText(recipeRow.getNameRecipe());
        tv2.setText(recipeRow.getCategory());
        tv3.setText(recipeRow.getPercentIng().toString() + " %");
        imageViewProfile.setImageResource(R.drawable.image_recipe);

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
    private final Filter mFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Recipe> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Recipe item : exampleList) {
                    if (item.getNameRecipe().toLowerCase().contains(filterPattern) || item.getCategory().toLowerCase().contains(filterPattern) ) {
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

}
