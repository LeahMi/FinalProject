package com.dvora.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.dvora.finalproject.entities.Category;
import com.dvora.finalproject.entities.Ingredient;
import com.dvora.finalproject.entities.IngredientInfo;
import com.dvora.finalproject.entities.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Repository {
    private int count = 0;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private HashMap hm = new HashMap();
    public static final String RECIPES_PATH = "userRecipes";
    public static final String INGREDIENTS_PATH = "userIngredients";
    public DatabaseReference ref;
    private StorageReference storageReference;
    public MutableLiveData<List<Recipe>> recipesData = new MutableLiveData<>();
    public MutableLiveData<Exception> exceptionsData = new MutableLiveData<>();
    public Repository() {
        FirebaseUser currentUser = FirebaseManager.currentUser;
        String uid = "";
        if (currentUser == null)
            uid = "admin";
        else
            uid = currentUser.getUid();
        ref = FirebaseManager.root.child(uid);
        ref.child(RECIPES_PATH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Recipe recipe = child.getValue(Recipe.class);
                    if (recipe != null)
                        recipes.add(recipe);
                }
                recipesData.postValue(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                exceptionsData.postValue(error.toException());
            }
        });
    }


    public void getRecipesForIngredient(String ingredientName, OnSearchRecipesByIngredient listener) {
        ref.child(INGREDIENTS_PATH)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Ingredient match = null;
                        for (DataSnapshot ingredientSnapShot : dataSnapshot.getChildren()) {
                            String databaseIngredientName = (String) ingredientSnapShot.child("name").getValue();
                            if (databaseIngredientName == null)
                                continue;
                            if (databaseIngredientName.equals(ingredientName)) {
                                match = ingredientSnapShot.getValue(Ingredient.class);
                                break;
                            }
                        }
                        if (match == null) {
                            System.out.println("No Recipes Found!");
                            listener.onNoRecipesFound("No recipes found for ingredient " + ingredientName);
                        } else {
                            Ingredient finalMatch = match;
                            ref.child(RECIPES_PATH)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            HashMap<String, Boolean> linkedRecipes = finalMatch.getLinkedRecipes();
                                            List<Recipe> matches = new ArrayList<>();
                                            for (DataSnapshot recipeSnapShot : dataSnapshot.getChildren()) {
                                                String recipeName = (String) recipeSnapShot.child("nameRecipe").getValue();
                                                if (recipeName == null)
                                                    continue;
                                                if (linkedRecipes.get(recipeName) != null &&
                                                        linkedRecipes.get(recipeName)) {
                                                    Recipe match = recipeSnapShot.getValue(Recipe.class);
                                                    if (match == null)
                                                        continue;

                                                    matches.add(match);
                                                }
                                            }
                                            if (matches.isEmpty()) {
                                                listener.onNoRecipesFound("No Recipes for ingredient " + ingredientName);
                                            } else {
                                                listener.onRecipesFound(matches);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            listener.onExceptionOcured(e);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onExceptionOcured(e);
                    }
                });
    }

    public void getAllRecipes(OnSearchAllRecipes listener) {
        getAllIngredients(new OnSearchAllIngredients() {
            @Override
            public void onIngredientsFound(List<Ingredient> matches) {
                int numOfIngredients = 0;
                for(Ingredient ingInventory :matches){
                    hm.put(ingInventory.getName(),Double.valueOf(df.format(ingInventory.getQuantity())));
                }
            }

            @Override
            public void onNoIngredientsFound(String message) { }

            @Override
            public void onExceptionOccurred(Exception e) { }
        });
        ref.child(RECIPES_PATH)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Recipe match = null;
                        List<Recipe> matches = new ArrayList<>();
                        for (DataSnapshot recipeSnapShot : dataSnapshot.getChildren()) {
                            match = recipeSnapShot.getValue(Recipe.class);
                            if (match == null)
                                continue;
                            else {
                                count = 0;
                                matches.add(match);
                                List<IngredientInfo> l=match.getIngredients();
                                int numOfIngs = l.size();
                                for(IngredientInfo ingredientInfo:l){
                                    if(!(hm.isEmpty())) {
                                        if (hm.containsKey(ingredientInfo.getName()) && ((Double)(hm.get(ingredientInfo.getName())))>=ingredientInfo.getQuantity()) {
                                            count++;

                                        }
                                    }
                                }
                                if(count != 0) {
                                    Log.d("count","count___ "+count);
                                    Log.d("numOfIngs","numOfIngs___ "+numOfIngs);
                                    Log.d("(count / numOfIngs)","(count / numOfIngs)____"+(count / numOfIngs));
                                    double d = (double)count / (double)numOfIngs;
                                    d = Double.valueOf(df.format(d));
                                    ref.child(RECIPES_PATH).child(recipeSnapShot.getKey()).child("percentIng").setValue(d * 100);
                                }
                            }
                            Log.d("TAG", "match===============" + match);
                        }
                        if (matches.isEmpty()) {
                            listener.onNoRecipesFound("No Recipes found");
                        } else {
                            listener.onRecipesFound(matches);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onExceptionOccurred(e);
                    }
                });
    }

    public void isExistIngredient(String name, OnAddNewIngredientListener listener) {
        DatabaseReference existingIngredient = ref.child(INGREDIENTS_PATH).child(name);
        existingIngredient.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    listener.onSuccess("true");
                else{
                    listener.onSuccess("false");
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });
    }
    public void getAllIngredients(OnSearchAllIngredients listener){
        ref.child(INGREDIENTS_PATH)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Ingredient match = null;
                        List<Ingredient> matches = new ArrayList<>();
                        for (DataSnapshot ingredientSnapShot : dataSnapshot.getChildren()) {
                            match = ingredientSnapShot.getValue(Ingredient.class);
                            if (match == null)
                                continue;
                            matches.add(match);
                            Log.d("TAG", "match===============" + match);
                        }

                        if(matches.isEmpty()) {
                            listener.onNoIngredientsFound("No Ingredients found");
                        } else {
                            listener.onIngredientsFound(matches);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onExceptionOccurred(e);
                    }
                });
    }

    public interface OnSearchAllIngredients {
        void onIngredientsFound(List<Ingredient> matches);

        void onNoIngredientsFound(String message);

        void onExceptionOccurred(Exception e);
    }

    public interface OnSearchAllRecipes {
        void onRecipesFound(List<Recipe> matches);

        void onNoRecipesFound(String message);

        void onExceptionOccurred(Exception e);
    }
    public interface OnSearchAllCategories{
        void onCategoriesFound(List<Category> matches);

        void onNoCategoriesFound(String message);

        void onExceptionOccurred(Exception e);
    }
    interface OnSearchRecipesByIngredient {
        void onRecipesFound(List<Recipe> matches);
        void onNoRecipesFound(String message);
        void onExceptionOcured(Exception e);
    }
    public interface OnAddNewRecipeListener {
        void onSuccess(String message);
        void onFailure(Exception e);
    }
    public interface OnAddNewIngredientListener{
        void onSuccess(String message);
        void onFailure(Exception e);
    }

    public interface OnSearchShoppingList{
        void onSuccess(String message);
        void onFailure(Exception e);
    }

    public interface OnSearchProfile{
        void onSuccess(String message);
        void onFailure(Exception e);
    }

    public void updateIngredient(Ingredient ingredient, OnSuccessListener listener){
        DatabaseReference existingIngredient = ref.child(INGREDIENTS_PATH).child(ingredient.getName());
        existingIngredient.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ref.child(INGREDIENTS_PATH).child(ingredient.getName()).setValue(ingredient);
                    listener.onSuccess("המוצר "+ingredient.getName()+ " עודכן במלאי ");
                }
            }
        });
    }
    public void saveNewIngredient(Ingredient ingredient,OnAddNewIngredientListener listener){
        DatabaseReference existingIngredient = ref.child(INGREDIENTS_PATH).child(ingredient.getName());
        existingIngredient.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    ref.child(INGREDIENTS_PATH).child(ingredient.getName()).setValue(ingredient);
                    listener.onSuccess("the ingredient "+ ingredient.getName() + " added");
                }
                else{
                    listener.onFailure(new Exception("the ingredient "+ingredient.getName()+" is exist"));
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });


    }

    public void saveNewRecipe(Recipe recipe, OnAddNewRecipeListener listener) {
        List<IngredientInfo> ingredients = recipe.getIngredients();
        for (IngredientInfo ing : ingredients) {
            DatabaseReference existingIngredient = ref.child(INGREDIENTS_PATH).child(ing.getName());
            existingIngredient
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                dataSnapshot.child("linkedRecipes")
                                        .child(recipe.getNameRecipe())
                                        .getRef()
                                        .setValue(true);
                            } else {
                                Ingredient ingredient = ing.getNewIngredientWithLink(recipe.getNameRecipe());
                                ingredient.setQuantity(0);
                                ref.child(INGREDIENTS_PATH)
                                        .child(ing.getName())
                                        .setValue(ingredient);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onFailure(e);
                        }
                    });

        }
        ref.child(RECIPES_PATH).push().setValue(recipe)
                .addOnSuccessListener(aVoid -> listener.onSuccess("Successfuly added " + recipe.getNameRecipe() + " to the list"))
                .addOnFailureListener(e -> listener.onFailure(e));
    }
    public void SaveListShopping(String listS){

        ref.child("ShoppingList").setValue(listS);
    }
    public void getList(OnSearchShoppingList listener)
    {
        ref.child("ShoppingList").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>(){
            public void onSuccess(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    String data=dataSnapshot.getValue().toString();
                    listener.onSuccess(data);
                }
                else {listener.onSuccess("Enter your Shopping List ");}
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });

    }
    public void getProfile(OnSearchProfile listener) {
        ref.child("userName").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String FullName = dataSnapshot.getValue().toString();
                    listener.onSuccess(FullName);

                }
            }
        });
    }
    public void addCategory(Category category, OnSuccessListener listener){
        ref.child("categories").child(category.getName()).setValue(category)
                .addOnSuccessListener(aVoid -> listener.onSuccess("Successfully added " + category.getName() + " to the categories"))
                .addOnFailureListener(e -> Log.d("no added",category.getName()+" no added to the categories"));
    }
    public void getAllCategories(OnSearchAllCategories listener){
        ref.child("categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Category match = null;
                        List<Category> matches = new ArrayList<>();
                        for (DataSnapshot categorySnapShot : dataSnapshot.getChildren()) {
                            match = categorySnapShot.getValue(Category.class);
                            if (match == null)
                                continue;
                            else {
                                matches.add(match);
                            }
                        }
                        if (matches.isEmpty()){
                            listener.onNoCategoriesFound("No categories found");
                        }else {
                            listener.onCategoriesFound(matches);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onExceptionOccurred(e);
                    }
                });

    }
    public void updateInventory(Recipe recipe,OnSuccessListener listener){
        getAllIngredients(new OnSearchAllIngredients() {
            @Override
            public void onIngredientsFound(List<Ingredient> matches) {
                List<IngredientInfo> list = recipe.getIngredients();
                for(IngredientInfo ing :list) {
                    for (Ingredient ingredientInventory : matches) {
                        if(ingredientInventory.getName().equals(ing.getName())){
                            ingredientInventory.setQuantity(ingredientInventory.getQuantity()-ing.getQuantity());
                            updateIngredient(ingredientInventory, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) { }
                            });
                        }
                    }
                }
                listener.onSuccess("המלאי עודכן");
            }

            @Override
            public void onNoIngredientsFound(String message) { }

            @Override
            public void onExceptionOccurred(Exception e) { }
        });
    }
    public MutableLiveData<Exception> getExceptionsData() {
        return exceptionsData;
    }

    public MutableLiveData<List<Recipe>> getRecipesData() {
        return recipesData;
    }
}
