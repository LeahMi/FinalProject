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


import com.dvora.finalproject.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends BaseAdapter implements Filterable {

    private final List<Recipe> data;
    private final List<Recipe> exampleList;
    private final LayoutInflater inflater;
    private final ICallbackAdapter iCallbackAdapter;
    private final List<String>filteredData = null;

    public ContactAdapter(List<Recipe> data, Context context, ICallbackAdapter callbackAdapter) {
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
        ImageView imageViewProfile = ROW.findViewById(R.id.img_profile);
        tv.setText(recipeRow.getNameRecipe());
        tv2.setText(recipeRow.getCategory());
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
//package com.dvora.finalproject;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//
//import com.dvora.finalproject.entities.Recipe;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ContactAdapter extends BaseAdapter implements Filterable {
//    private List<Recipe> data;
//    private LayoutInflater inflater;
//    private ICallbackAdapter iCallbackAdapter;
//    private ItemFilter mFilter = new ItemFilter();
//    private List<String>filteredData = null;
//
//    public ContactAdapter(List<Recipe> data, Context context, ICallbackAdapter callbackAdapter) {
//        this.data = data;
//        this.inflater = LayoutInflater.from(context);
//        this.iCallbackAdapter = callbackAdapter;
//    }
//
//
//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//
//    }
//
//    @Override
//    public View getView(int pos /* row index*/, View ROW /* reusable ROW */, ViewGroup viewGroup) {
//        if (ROW == null) {
//            ROW = inflater.inflate(R.layout.main_list_row, null);
//            Log.d("TAG", "creating new row ROW");
//        } else {
//            Log.d("TAG", "reusing old row ROW");
//        }
//
//        final Recipe recipeRow = data.get(pos);
//        TextView tv = ROW.findViewById(R.id.mainlistrow_text_v);
//        ImageView imageViewProfile = ROW.findViewById(R.id.img_profile);
//        tv.setText(recipeRow.getNameRecipe() + "\n" + recipeRow.getCategory());
//        imageViewProfile.setImageResource(R.drawable.image_recipe);
//
//        ROW.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("asds", "ROW");
//
//                if (iCallbackAdapter != null) {
//                    iCallbackAdapter.onClickItem(recipeRow);
//                }
//            }
//        });
//        return ROW;
//    }
//
//    @Override
//    public Filter getFilter() {
//        return mFilter;
//    }
//    private class ItemFilter extends Filter {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//
//            String filterString = constraint.toString().toLowerCase();
//
//            FilterResults results = new FilterResults();
//
//            final List<String> list=null;
//            for (Recipe recipe:data) {
//                list.add(recipe.getNameRecipe());
//            }
//            int count = list.size();
//            final ArrayList<String> nlist = new ArrayList<String>(count);
//
//            String filterableString ;
//
//            for (int i = 0; i < count; i++) {
//                filterableString = list.get(i);
//                if (filterableString.toLowerCase().contains(filterString)) {
//                    nlist.add(filterableString);
//                }
//            }
//
//            results.values = nlist;
//            results.count = nlist.size();
//
//            return results;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            filteredData = (ArrayList<String>) results.values;
//            notifyDataSetChanged();
//        }
//
//    }
//}