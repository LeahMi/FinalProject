package com.dvora.finalproject;

import android.widget.EditText;

import java.util.List;

public class Recipe {
    private String nameRecipe;
    private String category;
    private String preparationTime;
    private List<IngredientInfo> ingredientInfo;
    private String preparationMethod;

    public Recipe() {
    }

    public Recipe(String nameRecipe, String category, String preparationTime, List<IngredientInfo> ingredientInfo, String preparationMethod) {
        this.nameRecipe = nameRecipe;
        this.category = category;
        this.preparationTime = preparationTime;
        this.ingredientInfo = ingredientInfo;
        this.preparationMethod = preparationMethod;
    }

    public void setNameRecipe(String nameRecipe) {
        this.nameRecipe = nameRecipe;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setIngredients(List<IngredientInfo> ingredientInfo) {
        this.ingredientInfo = ingredientInfo;
    }

    public void setPreparationMethod(String preparationMethod) {
        this.preparationMethod = preparationMethod;
    }

    public String getNameRecipe() {
        return nameRecipe;
    }

    public String getCategory() {
        return category;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public List<IngredientInfo> getIngredients() {
        return ingredientInfo;
    }

    public String getPreparationMethod() {
        return preparationMethod;
    }
}
