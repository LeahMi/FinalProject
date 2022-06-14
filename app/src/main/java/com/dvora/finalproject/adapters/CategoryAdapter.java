package com.dvora.finalproject.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.dvora.finalproject.ICallBackAdapterCategory;
import com.dvora.finalproject.ICallbackAdapter;
import com.dvora.finalproject.R;
import com.dvora.finalproject.entities.Category;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final Context context;
    private final List<Category> data;
    private final ICallBackAdapterCategory callBackAdapterC;

    public CategoryAdapter(Context context, List<Category> data, ICallBackAdapterCategory callBackAdapterC ) {
        this.context = context;
        this.data = data;
        this.callBackAdapterC = callBackAdapterC;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.row_category,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Name.setText(data.get(position).getNumOfRecipes()+ " מתכונים");
        holder.button.setText(data.get(position).getName());
        if(data.get(position).getImgUrl()!=null) {
            Glide.with(context)
                    .load(data.get(position).getImgUrl())
                    .into(holder.image);
        }else {
            //holder.image.setImageResource(data.get(position).getImgUrl());
            holder.image.setImageResource(R.drawable.images);
            //holder.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackAdapterC != null) {
                    callBackAdapterC.onClickItem(data.get(position));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Name;
        ImageView image;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.tv_name_category);
            image = itemView.findViewById(R.id.imageView);
            button =itemView.findViewById(R.id.button2);
        }
    }



}
