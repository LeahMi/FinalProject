package com.dvora.finalproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvora.finalproject.entities.Ingredient;
import com.dvora.finalproject.entities.IngredientInfo;
import com.dvora.finalproject.entities.Recipe;

import java.util.List;

import javax.sql.RowSetWriter;

public class IngAdapter extends BaseAdapter {
    private List<Ingredient> dataIng;
    private LayoutInflater inflaterI;
    public IngAdapter(List<Ingredient> dataIng, Context context)
    {
        this.dataIng=dataIng;
        this.inflaterI=LayoutInflater.from(context);;
    }


    @Override
    public int getCount() {
        return dataIng.size();
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
    public View getView(int i, View Row, ViewGroup viewGroup) {
        if(Row==null){
            Row = inflaterI.inflate(R.layout.inventory_list_row, null);
            Log.d("TAG", "creating new row ROW");
        }
        else {
            Log.d("TAG", "reusing old row ROW");
        }
        final Ingredient ingredientRow=dataIng.get(i);
        TextView tv = Row.findViewById(R.id.inventorylistrow_text_v);
        TextView tv_quantity = Row.findViewById(R.id.inventorylistrow_quantity);
        tv.setText(ingredientRow.getName());
        tv_quantity.setText(ingredientRow.getQuantity()+" "+ingredientRow.getType());
        return Row;
    }

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


}
