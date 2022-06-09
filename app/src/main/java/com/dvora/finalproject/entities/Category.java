package com.dvora.finalproject.entities;

import java.io.Serializable;

public class Category implements Serializable {

    private String imgUrl;
    private String name;
    private int numOfRecipes;

    public Category(){
        this.name = "כל המתכונים";
        this.imgUrl = null;
    }
    public Category(String imgUrl, String name, int numOfRecipes) {
        this.name = name;
        this.imgUrl =imgUrl;
        this.numOfRecipes = numOfRecipes;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public int getNumOfRecipes() {
        return numOfRecipes;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getName() {
        return name;
    }

//    public int getImage() {
//        return image;
//    }
    public void incNumOfRecipes(){
        numOfRecipes++;
    }
    public void setNumOfRecipes(int numOfRecipes){
        this.numOfRecipes = numOfRecipes;
    }

}

