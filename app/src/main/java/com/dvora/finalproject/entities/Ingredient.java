package com.dvora.finalproject.entities;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class Ingredient {
    private String name;
    private double quantity;
    private String type;
    private HashMap<String,Boolean> linkedRecipes; // ids


    public Ingredient(String name, Double q, HashMap<String, Boolean> linkedRecipes, String[] type) {}

    public Ingredient() {}


    public Ingredient(String name, double quantity, HashMap<String, Boolean> linkedRecipes,String type) {
        this.name=name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        this.quantity = quantity;
        this.type = type;
        this.linkedRecipes = linkedRecipes;
    }

    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public double getQuantity() {
        return quantity;
    }

    public HashMap<String,Boolean> getLinkedRecipes() {
        return linkedRecipes;
    }

    @Exclude
    public IngredientInfo getIngredientInfo() {
        return new IngredientInfo(name,quantity,type);
    }

    public void setLinkedRecipes(HashMap<String,Boolean> linkedRecipes) {
        this.linkedRecipes = linkedRecipes;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setQuantity(double quantity) {
        BigDecimal newQuantity = new BigDecimal(quantity).setScale(2, RoundingMode.HALF_UP);
        this.quantity = newQuantity.doubleValue();
        //this.quantity = Math.round((quantity * 100.0) / 100.0);
    }

    @Override
    public String toString() {
        return name + '\n' +
                "quantity: " + quantity;
    }
}
