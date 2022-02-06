package com.dvora.finalproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class ContactAdapter extends BaseAdapter {

    private List<Recipe> data;
    private LayoutInflater inflater;
    private ICallbackAdapter iCallbackAdapter;

    public ContactAdapter(List<Recipe> data, Context context, ICallbackAdapter callbackAdapter) {
        this.data = data;
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
        ImageView imageViewProfile = ROW.findViewById(R.id.img_profile);
        tv.setText(recipeRow.getNameRecipe() + "\n" + recipeRow.getCategory());
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
}