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
    private boolean flag = false;
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
                            System.out.println("לא נמצאו מתכונים!");
                            listener.onNoRecipesFound("לא נמצא מתכונים עם המוצר " + ingredientName);
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
                for (Ingredient ingInventory : matches) {
//                    hm.put(ingInventory.getName(),Double.valueOf(df.format(ingInventory.getQuantity())));
                    hm.put(ingInventory.getName(), ingInventory);
                }
            }

            @Override
            public void onNoIngredientsFound(String message) {
            }

            @Override
            public void onExceptionOccurred(Exception e) {
            }
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
                                List<IngredientInfo> l = match.getIngredients();
                                int numOfIngs = l.size();
                                for (IngredientInfo ingredientInfo : l) {
                                    if (!(hm.isEmpty())) {
//                                        if (hm.containsKey(ingredientInfo.getName()) && ((Double)(hm.get(ingredientInfo.getName())))>=ingredientInfo.getQuantity()) {
//                                            count++;
                                        if (hm.containsKey(ingredientInfo.getName())) {
                                            Ingredient ingInventory = (Ingredient) (hm.get(ingredientInfo.getName()));
                                            Double quantity = convert(ingInventory, ingredientInfo);
                                            if (ingInventory.getQuantity() >= quantity)
                                                count++;
                                        }
                                    }
                                }
                                if (count != 0) {
                                    Log.d("count", "count___ " + count);
                                    Log.d("numOfIngs", "numOfIngs___ " + numOfIngs);
                                    Log.d("(count / numOfIngs)", "(count / numOfIngs)____" + (count / numOfIngs));
                                    double d = (double) count / (double) numOfIngs;
                                    d = Double.valueOf(df.format(d));
                                    d = Double.valueOf(df.format(d * 100));
                                    System.out.println("d:   " + d);
                                    ref.child(RECIPES_PATH).child(recipeSnapShot.getKey()).child("percentIng").setValue(d);
                                } else {
                                    ref.child(RECIPES_PATH).child(recipeSnapShot.getKey()).child("percentIng").setValue(0);
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
                if (dataSnapshot.exists())
                    listener.onSuccess("true");
                else {
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

    public void getAllIngredients(OnSearchAllIngredients listener) {
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

                        if (matches.isEmpty()) {
                            listener.onNoIngredientsFound("לא נמצאו מרכיבים");
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

    public interface OnSearchAllCategories {
        void onCategoriesFound(List<Category> matches);

        void onNoCategoriesFound(String message);

        void onExceptionOccurred(Exception e);
    }

    public interface OnSearchRecipesByIngredient {
        void onRecipesFound(List<Recipe> matches);

        void onNoRecipesFound(String message);

        void onExceptionOcured(Exception e);
    }

    public interface OnAddNewRecipeListener {
        void onSuccess(String message);

        void onFailure(Exception e);
    }

    public interface OnAddNewIngredientListener {
        void onSuccess(String message);

        void onFailure(Exception e);
    }

    public interface OnSearchShoppingList {
        void onSuccess(String message);

        void onFailure(Exception e);
    }

    public interface OnSearchProfile {
        void onSuccess(String message);

        void onFailure(Exception e);
    }

    public void updateIngredient(Ingredient ingredient, OnSuccessListener listener) {
        DatabaseReference existingIngredient = ref.child(INGREDIENTS_PATH).child(ingredient.getName());
        existingIngredient.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ref.child(INGREDIENTS_PATH).child(ingredient.getName()).setValue(ingredient);
                    listener.onSuccess("המוצר " + ingredient.getName() + " עודכן במלאי ");
                }
            }
        });
    }

    public void saveNewIngredient(Ingredient ingredient, OnAddNewIngredientListener listener) {
        DatabaseReference existingIngredient = ref.child(INGREDIENTS_PATH).child(ingredient.getName());
        existingIngredient.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    ref.child(INGREDIENTS_PATH).child(ingredient.getName()).setValue(ingredient);
                    listener.onSuccess(" המוצר " + ingredient.getName() + " התווסף ");

                } else {
                    listener.onFailure(new Exception("המוצר " + ingredient.getName() + " קיים "));
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
        getAllRecipes( new OnSearchAllRecipes(){
            @Override
            public void onRecipesFound(List<Recipe> matches) {
               flag = false;
               for (Recipe r:matches){
                   if (r.getNameRecipe().equals(recipe.getNameRecipe())) {
                       Log.v("flag","flag");
                       flag = true;
                   }
               }
                ref.child(RECIPES_PATH).child(recipe.getNameRecipe()).setValue(recipe)
                        .addOnSuccessListener(aVoid -> listener.onSuccess("נוסף בהצלחה " + recipe.getNameRecipe() + " לרשימה"))
                        .addOnFailureListener(e -> listener.onFailure(e));
                if(!flag) {
                    ref.child("categories").child(recipe.getCategory()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            Category category = dataSnapshot.getValue(Category.class);
                            Log.v("flag1","flag1");
                            category.incNumOfRecipes();
                            ref.child("categories").child(recipe.getCategory()).setValue(category);
                        }
                    });
                }

            }
            @Override
            public void onNoRecipesFound(String message) {
                ref.child(RECIPES_PATH).child(recipe.getNameRecipe()).setValue(recipe)
                        .addOnSuccessListener(aVoid -> listener.onSuccess("נוסף בהצלחה " + recipe.getNameRecipe() + " לרשימה"))
                        .addOnFailureListener(e -> listener.onFailure(e));
                ref.child("categories").child(recipe.getCategory()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Category category = dataSnapshot.getValue(Category.class);
                        category.incNumOfRecipes();
                        ref.child("categories").child(recipe.getCategory()).setValue(category);
                    }
                });

            }
            @Override
            public void onExceptionOccurred(Exception e) { }
        });
    }

    public void SaveListShopping(String listS) {

        ref.child("ShoppingList").setValue(listS);
    }

    public void getList(OnSearchShoppingList listener) {
        ref.child("ShoppingList").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            public void onSuccess(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String data = dataSnapshot.getValue().toString();
                    listener.onSuccess(data);
                } else {
                    listener.onSuccess("הכנס את רשימת הקניות שלך ");
                }
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

    public void addCategory(Category category, OnSuccessListener listener) {
        ref.child("categories").child(category.getName()).setValue(category)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (listener != null)
                            listener.onSuccess("נוסף בהצלחה " + category.getName() + " לקטגוריות ");
                    }
                })
                .addOnFailureListener(e -> Log.d("no added", category.getName() + " לא נוסף לקטגוריות "));
    }

    public void getAllCategories(OnSearchAllCategories listener) {
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
                        if (matches.isEmpty()) {
                            listener.onNoCategoriesFound("No categories found");
                        } else {
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

    public void updateInventory(Recipe recipe, OnSuccessListener listener) {
        getAllIngredients(new OnSearchAllIngredients() {
            @Override
            public void onIngredientsFound(List<Ingredient> matches) {
                List<IngredientInfo> list = recipe.getIngredients();
                for (IngredientInfo ing : list) {
                    for (Ingredient ingredientInventory : matches) {
                        if (ingredientInventory.getName().equals(ing.getName())) {
//                            if(ingredientInventory.getType().equals(ing.getType())){
//
//                            }
//                            else{
                            Double amount = convert(ingredientInventory, ing);
//                            }
                            ingredientInventory.setQuantity(ingredientInventory.getQuantity() - amount);
                            updateIngredient(ingredientInventory, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                }
                            });
                        }
                    }
                }
                listener.onSuccess("המלאי עודכן");
            }

            @Override
            public void onNoIngredientsFound(String message) {
            }

            @Override
            public void onExceptionOccurred(Exception e) {
            }
        });
    }

    Double convert(Ingredient ingInventory, IngredientInfo ingRecipe) {
        Double amount = 0.0;
        switch (ingRecipe.getName()) {
            case "מלפפון":
            case "עגבניה":
            case "בצל":
            case "כרישה":
            case "תפוח אדמה":
                amount = convertGrUnit(ingInventory, ingRecipe, 200.0);
                break;
            case "גזר":
                amount = convertGrUnit(ingInventory, ingRecipe, 100.0);
                break;
            case "חציל":
                amount = convertGrUnit(ingInventory, ingRecipe, 350.0);
                break;
            case "קישוא":
                amount = convertGrUnit(ingInventory, ingRecipe, 170.0);
                break;
            case "בטטה":
                amount = convertGrUnit(ingInventory, ingRecipe, 290.0);
                break;
            case "דלורית":
                amount = convertGrUnit(ingInventory, ingRecipe, 700.0);
                break;
            case "ברוקולי":
                amount = convertGrUnit(ingInventory, ingRecipe, 600.0);
                break;
            case "קולורבי":
                amount = convertGrUnit(ingInventory, ingRecipe, 400.0);
                break;
            case "כרוב":
                amount = convertGrUnit(ingInventory, ingRecipe, 1500.0);
                break;
            case "גמבה":
                amount = convertGrUnit(ingInventory, ingRecipe, 150.0);
                break;
            case "לימון":
                amount = convertGrUnit(ingInventory, ingRecipe, 180.0);
                break;
            case "סלק":
                amount = convertGrUnit(ingInventory, ingRecipe, 270.0);
                break;
            case "שמן":
            case "חלב":
                amount = convertMlUnit(ingInventory, ingRecipe, 1000.0);
                break;
            default:
                amount = ingRecipe.getQuantity();
                break;
        }
        return amount;
    }

    public Double convertGrUnit(Ingredient ingInventory, IngredientInfo ingRecipe, Double quantity) {
        Double amount = 0.0;
        switch (ingInventory.getType()) {
            case "גרם":
                switch (ingRecipe.getType()) {
                    case "גרם":
                        amount = ingRecipe.getQuantity();
                        break;
                    case "יחידה":
                        amount = ingRecipe.getQuantity() * quantity;
                        break;
                }
                break;
            case "יחידה":
                switch (ingRecipe.getType()) {
                    case "יחידה":
                        amount = ingRecipe.getQuantity();
                        break;
                    case "גרם":
                        amount = ingRecipe.getQuantity() / quantity;
                        break;
                }
                break;
        }
        return amount;
    }

    public Double convertMlUnit(Ingredient ingInventory, IngredientInfo ingRecipe, Double quantity) {
        Double amount = 0.0;
        switch (ingInventory.getType()) {
            case "מל":
                switch (ingRecipe.getType()) {
                    case "מל":
                        amount = ingRecipe.getQuantity();
                        break;
                    case "יחידה":
                        amount = ingRecipe.getQuantity() * quantity;
                        break;
                }
                break;
            case "יחידה":
                switch (ingRecipe.getType()) {
                    case "יחידה":
                        amount = ingRecipe.getQuantity();
                        break;
                    case "מל":
                        amount = ingRecipe.getQuantity() / quantity;
                        break;
                }
                break;
        }
        return amount;
    }

    public MutableLiveData<Exception> getExceptionsData() {
        return exceptionsData;
    }

    public MutableLiveData<List<Recipe>> getRecipesData() {
        return recipesData;
    }
}
