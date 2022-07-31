package com.dvora.finalproject.entities;

import com.dvora.finalproject.entities.Ingredient;
import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class IngredientInfo {
    private String name;
    private double quantity;
    private String type;


    public IngredientInfo() {}

    public IngredientInfo(String name, double quantity, String type) {
        this.name=name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        this.quantity = quantity;
        this.type = type;
    }

    @Exclude
    public Ingredient getNewIngredientWithLink(String recipeLink) {
        HashMap<String,Boolean> linkedRecipes = new HashMap<>();
        linkedRecipes.put(recipeLink,true);
        return new Ingredient(name,quantity,linkedRecipes,type);
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

    public void setName(String name) {
        this.name = name;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name+" "+quantity+'\n';
    }
}
