package com.dvora.finalproject.entities;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String email;
    private String password;
    private String fullName;
    private List<Ingredient> inventory;
    private List<Recipe> recipes;

    public User() {

    }

    public User(String email, String password, String fullName, List<Ingredient> ingredients,List<Recipe> recipes) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public User(String email, String password,String fullName) {
        this.email=email;
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }
}
