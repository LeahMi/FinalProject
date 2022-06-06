package com.dvora.finalproject.entities;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    private String nameRecipe;
    private String category;
    private String preparationTime;
    private List<IngredientInfo> ingredientInfo;
    private String preparationMethod;
    private Double percentIng = 0.0;
    private String imgUrl;
    private String level;

    public Recipe() {
    }

    public Recipe(String nameRecipe, String category, String preparationTime, List<IngredientInfo> ingredientInfo, String preparationMethod) {
        this.nameRecipe = nameRecipe;
        this.category = category;
        this.preparationTime = preparationTime;
        this.ingredientInfo = ingredientInfo;
        this.preparationMethod = preparationMethod;
    }
    public Recipe(String nameRecipe, String category, String preparationTime, List<IngredientInfo> ingredientInfo, String preparationMethod, Double percentIng) {
        this.nameRecipe = nameRecipe;
        this.category = category;
        this.preparationTime = preparationTime;
        this.ingredientInfo = ingredientInfo;
        this.preparationMethod = preparationMethod;
        this.percentIng = percentIng;
    }
    public Recipe(String nameRecipe, String category, String preparationTime, List<IngredientInfo> ingredientInfo, String preparationMethod, Double percentIng, String imgUrl) {
        this.nameRecipe = nameRecipe;
        this.category = category;
        this.preparationTime = preparationTime;
        this.ingredientInfo = ingredientInfo;
        this.preparationMethod = preparationMethod;
        this.percentIng = percentIng;
        this.imgUrl = imgUrl;
    }
    public Recipe(String nameRecipe, String category, String preparationTime, List<IngredientInfo> ingredientInfo, String preparationMethod, Double percentIng, String imgUrl, String level) {
        this.nameRecipe = nameRecipe;
        this.category = category;
        this.preparationTime = preparationTime;
        this.ingredientInfo = ingredientInfo;
        this.preparationMethod = preparationMethod;
        this.percentIng = percentIng;
        this.imgUrl = imgUrl;
        this.level = level;
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

    public void setPercentIng(Double percentIng) {
        this.percentIng = percentIng;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public Double getPercentIng() { return percentIng; }

    public String getImgUrl() { return imgUrl; }

    public String getLevel() { return level; }

    @Override
    public String toString() {
        return "Recipe:" +'\n'+
                " nameRecipe: " + nameRecipe +'\n'+
                " category: " + category +'\n'+
                " preparationTime: " + preparationTime +'\n'+
                " ingredientInfo: " + ingredientInfo +'\n'+
                " preparationMethod: " + preparationMethod+'\n'
                ;
    }

}
