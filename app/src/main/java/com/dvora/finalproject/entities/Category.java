package com.dvora.finalproject.entities;

import java.io.Serializable;

public class Category implements Serializable {
//    private int image = 0;
    private String imgUrl;
    private String name;
    private int numOfRecipes;

    public Category(){}
    public Category(String imgUrl, String name, int numOfRecipes) {
        this.name = name;
        this.imgUrl =imgUrl;
        this.numOfRecipes = numOfRecipes;
    }

    //    public Category(String name, int image) {
//        this.Name = name;
//        this.image = image;
//        this.numOfRecipes = 0;
//    }
//    public Category(String name) {
//        this.name = name;
//        this.numOfRecipes = 0;
//    }
//    public Category(String name, String imgUrl) {
//        this.name = name;
//        this.imgUrl =imgUrl;
//        this.numOfRecipes = 0;
//    }

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


}

