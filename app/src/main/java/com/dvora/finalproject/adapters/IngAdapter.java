package com.dvora.finalproject.adapters;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.entities.Ingredient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.parseColor;
import static android.graphics.Color.rgb;

public class IngAdapter extends BaseAdapter {
    private List<Ingredient> dataIng;
    private List<Ingredient> data;
    private LayoutInflater inflaterI;
    private Repository repo = new Repository();
    public IngAdapter(List<Ingredient> dataIng, Context context)
    {
        this.dataIng = dataIng;
        this.data = new ArrayList<>(dataIng);
        this.inflaterI = LayoutInflater.from(context);
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
        final Ingredient ingredientRow = dataIng.get(i);
        TextView tv = Row.findViewById(R.id.inventorylistrow_text_v);
        TextView tv_type = Row.findViewById(R.id.inventorylistrow_type);
        EditText edt_quantity = Row.findViewById(R.id.edt_quantity);
        ImageButton btn_edit = Row.findViewById(R.id.btn_edit);
        tv.setText(ingredientRow.getName());
        tv_type.setText(ingredientRow.getType());
        edt_quantity.setText(ingredientRow.getQuantity()+"");
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_quantity.setEnabled(true);
                edt_quantity.setTextColor(rgb(255,165,0));
            }
        });

        edt_quantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER){
                    try{
                        Double d = Double.parseDouble(edt_quantity.getText().toString());
                        if(d>=0) {
                            edt_quantity.setTextColor(parseColor("#00DDFF"));
                            edt_quantity.setEnabled(false);
                            ingredientRow.setQuantity(d);
                            repo.updateIngredient(ingredientRow, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(inflaterI.getContext(), o.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            edt_quantity.setError("נא הזן מספר מ0 ומעלה");
                        }
                    }catch (NumberFormatException e){
                        edt_quantity.setError("הערך לא חוקי נא הזן מספר מ0 ומעלה");
                    }
                }
                return false;
            }
        });


        return Row;
    }

    public Filter getFilter() {
        return mFilter;
    }
    private final Filter mFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Ingredient> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(data);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Ingredient item : data) {
                    if (item.getName().toLowerCase().contains(filterPattern)  ) {
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
            dataIng.clear();
            dataIng.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
