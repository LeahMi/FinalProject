package com.dvora.finalproject.entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class Ingredient {
    private String name;
    private double quantity;
    private HashMap<String,Boolean> linkedRecipes; // ids


    public Ingredient() {}

    public Ingredient(String name, double quantity, HashMap<String, Boolean> linkedRecipes) {
        this.name = name;
        this.quantity = quantity;
        this.linkedRecipes = linkedRecipes;
    }

    public String getName() {
        return name;
    }
    public double getQuantity() {
        return quantity;
    }

    public HashMap<String,Boolean> getLinkedRecipes() {
        return linkedRecipes;
    }

    @Exclude
    public IngredientInfo getIngredientInfo() {
        return new IngredientInfo(name,quantity);
    }

    public void setLinkedRecipes(HashMap<String,Boolean> linkedRecipes) {
        this.linkedRecipes = linkedRecipes;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name + '\n' +
                "quantity: " + quantity;
    }
}
