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
        tv.setText(recipeRow.getNameRecipe());
        tv2.setText(recipeRow.getCategory());
        tv3.setText(recipeRow.getPercentIng().toString() + " %");
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
                filteredList.addAll(exampleList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Recipe item : exampleList) {
                    if (item.getNameRecipe().toLowerCase().contains(filterPattern) ||
                            item.getCategory().toLowerCase().contains(filterPattern) ||
                            item.getPercentIng().toString().toLowerCase().contains(filterPattern) ||
                            item.getLevel().toLowerCase().contains(filterPattern) ||
                            item.getPreparationTime().toLowerCase().contains(filterPattern) ||
                            item.getIngredientInfoToString().toLowerCase().contains(filterPattern)) {
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