package com.dvora.finalproject.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dvora.finalproject.FirebaseManager;
import com.dvora.finalproject.ListRecipesFragment;
import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.entities.Category;
import com.dvora.finalproject.entities.Ingredient;
import com.dvora.finalproject.entities.IngredientInfo;
import com.dvora.finalproject.entities.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddRecipeFragment extends BaseFragment implements DialogIng.OnInputSelected{
    public final static String CATEGORY_KEY = "CATEGORY_KEY";
    public final static String RECIPE_KEY = "RECIPE_KEY";
    private ImageView imageView;
    private Category category1;
    private Recipe existRecipe;
    private AutoCompleteTextView textIn;
    private EditText quantity, prep, name;
    private Button buttonSave, btnUpload;
    private ImageButton buttonAdd;
    private String qua, ing, type, level, Time, nameCategory;
    private IngredientInfo ingr;
    private String[] times = {"זמן הכנה","10 דק'","15 דק'","30 דק'","45 דק'","שעה","שעה +"};
    private String[] types = {"גרם","קורט","מל","יחידה","כפית","כף","כוס"};
    private String[] levels = {"דרגת קושי","קל","בינוני","קשה"};
    String all = "";
    List<String> spinnerlist, spinnerListTime, spinnerListCategory;
    ArrayAdapter<String> arrayadapter, arrayadapterTimes, arrayAdapterCategories;
    private String[] myListIng;
    private List<IngredientInfo> allIngredients = new ArrayList<>();
    private TextView mInputDisplay;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private final int IMG_REQUEST_ID = 10;
    private Uri imgUri;
    private Repository repo = new Repository();
    List<Category> matches1 = new ArrayList<>();

    public static Bundle newInstance(Category category) {
         Bundle args = new Bundle();
         args.putSerializable(CATEGORY_KEY, category);
         return args;
    }
    public static Bundle newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putSerializable(RECIPE_KEY, recipe);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            Bundle bundle = getArguments();
            if(!(bundle.getSerializable((CATEGORY_KEY)) ==null))
                category1 = (Category) bundle.getSerializable(CATEGORY_KEY);
            else
                existRecipe = (Recipe) bundle.getSerializable(RECIPE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        LinearLayout container2 = (LinearLayout) v.findViewById(R.id.container);
        TextView title = v.findViewById(R.id.title_add_recipe);
        if(!(existRecipe == null)){
            title.setText("עריכת מתכון");
            List<IngredientInfo> l = existRecipe.getIngredients();
            for (IngredientInfo ing:l){
                LayoutInflater layoutInflater =
                        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row, null);
                TextView textOut = (TextView) addView.findViewById(R.id.textout);
                TextView textOut2 = (TextView) addView.findViewById(R.id.textout2);
                TextView textOutType = (TextView) addView.findViewById(R.id.textout3);
                textOut.setText(ing.getName());
                textOut2.setText(String.valueOf(ing.getQuantity()));
                textOutType.setText(ing.getType());
                allIngredients.add(ing);
                ImageButton buttonRemove = (ImageButton) addView.findViewById(R.id.remove);
                buttonRemove.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) addView.getParent()).removeView(addView);
                        for (int i = 0; i < allIngredients.size(); ++i) {
                            if (allIngredients.get(i).getName().equals(textOut.getText().toString()))
                                allIngredients.remove(i);
                        }
                    }
                });

                container2.addView(addView);
            }
        }
        Spinner spinnerCategory = (Spinner) v.findViewById(R.id.spinner_category);

        repo.getAllCategories(new Repository.OnSearchAllCategories() {
            @Override
            public void onCategoriesFound(List<Category> matches) {
                matches1.addAll(matches);
                Log.e("size ","size " + matches1.size());
                Log.e("aaaaaaaaaaa "," "+matches1.get(0));
                String[] categories = new String[matches1.size()+1];
                categories[0] = "קטגוריה";
                for(int i=1; i<=matches1.size(); ++i){
                    categories[i] = matches1.get(i-1).getName();
                }
                spinnerListCategory = new ArrayList<>(Arrays.asList(categories));
                arrayAdapterCategories = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, spinnerListCategory){
                    @Override
                    public boolean isEnabled(int position){
                        if(position == 0) {
                            //Disable the first item of spinner.
                            return false;
                        }
                        else
                            return true;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,ViewGroup parent)
                    {
                        View spinnerview = super.getDropDownView(position, convertView, parent);

                        TextView spinnertextview = (TextView) spinnerview;
                        if(position == 0) {
                            //Set the disable spinner item color fade .
                            spinnertextview.setTextColor(Color.parseColor("#bcbcbb"));
                        }
                        else
                            spinnertextview.setTextColor(Color.BLACK);
                        return spinnerview;
                    }
                };

                arrayAdapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_item);

                spinnerCategory.setAdapter(arrayAdapterCategories);
                spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.v("item", (String) parent.getItemAtPosition(position));
                        nameCategory = (String)parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                if(!(existRecipe==null)){
                    spinnerCategory.setVisibility(View.VISIBLE);
                    int i = spinnerListCategory.indexOf(existRecipe.getCategory());
                    spinnerCategory.setSelection(i);
                }
            }
            @Override
            public void onNoCategoriesFound(String message) {
            }
            @Override
            public void onExceptionOccurred(Exception e) {
            }
        });
        if(!(category1==null)) {
            if (category1.getName().equals("כל המתכונים")) {
                spinnerCategory.setVisibility(View.VISIBLE);
            } else {
                nameCategory = category1.getName();
            }
        }
        else
            nameCategory = existRecipe.getCategory();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageView = (ImageView) v.findViewById(R.id.image_view_recipe_img);
        Spinner spinner =(Spinner) v.findViewById(R.id.spinner2);
        Spinner spinnerLevel = (Spinner) v.findViewById(R.id.spinner_level);
        Spinner spinnerTime = (Spinner) v.findViewById(R.id.spinner_time); 
        spinnerlist = new ArrayList<>(Arrays.asList(levels));
        spinnerListTime = new ArrayList<>(Arrays.asList(times));
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, levels);
//        spinnerLevel.setAdapter(adapter1);
        /////////

        arrayadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinnerlist){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    //Disable the first item of spinner.
                    return false;
                }
                else
                    return true;
            }
            @Override
            public View getDropDownView(int position, View convertView,ViewGroup parent)
            {
                View spinnerview = super.getDropDownView(position, convertView, parent);

                TextView spinnertextview = (TextView) spinnerview;

                if (position == 0) {
                    //Set the disable spinner item color fade .
                    spinnertextview.setTextColor(Color.parseColor("#bcbcbb"));
                } else
                    spinnertextview.setTextColor(Color.BLACK);

                return spinnerview;
            }
        };

        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerLevel.setAdapter(arrayadapter);
        /////////
        spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                level = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        if(!(existRecipe==null)){
            int i =Arrays.asList(levels).indexOf(existRecipe.getLevel());
            spinnerLevel.setSelection(i);
        }
        ///////////////////////////
        arrayadapterTimes = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinnerListTime){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    //Disable the first item of spinner.
                    return false;
                }
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, View convertView,ViewGroup parent)
            {
                View spinnerview = super.getDropDownView(position, convertView, parent);

                TextView spinnertextview = (TextView) spinnerview;

                if(position == 0) {
                    //Set the disable spinner item color fade .
                    spinnertextview.setTextColor(Color.parseColor("#bcbcbb"));
                }
                else
                    spinnertextview.setTextColor(Color.BLACK);
                return spinnerview;
            }
        };

        arrayadapterTimes.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerTime.setAdapter(arrayadapterTimes);
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                Time = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        if(!(existRecipe==null)){
            int i =Arrays.asList(times).indexOf(existRecipe.getPreparationTime());
            spinnerTime.setSelection(i);
        }
        ///////////////////////////

//        Toast.makeText(getContext(),category1.getName(),Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                type = (String)parent.getItemAtPosition(position);
                //((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        ScrollView mainScroll = (ScrollView) v.findViewById(R.id.mainScroll);
        textIn = (AutoCompleteTextView) v.findViewById(R.id.textin);
        repo.getAllIngredients(new Repository.OnSearchAllIngredients() {
            String[] emptyList = new String[]{};
            @Override
            public void onIngredientsFound(List <Ingredient> matches) {
                myListIng = new String[matches.size()];
                for(int i=0; i<matches.size() ; ++i){
                    myListIng[i] = matches.get(i).getName();
                    Log.d("myListIng[i]","myListIng ["+i+"]"+" "+myListIng[i]);
                }
                textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, myListIng));
            }
            @Override
            public void onNoIngredientsFound(String message) {
                textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, emptyList));
            }
            @Override
            public void onExceptionOccurred(Exception e) {
                textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, emptyList));
            }
        });


        quantity = (EditText) v.findViewById(R.id.quantity);
        name = (EditText) v.findViewById(R.id.name);
        if(!(existRecipe == null)){
            name.setVisibility(View.INVISIBLE);
        }
        prep = (EditText) v.findViewById(R.id.prep);
        if(!(existRecipe == null)){
            prep.setText(existRecipe.getPreparationMethod());
        }
        buttonAdd = (ImageButton) v.findViewById(R.id.add);
        buttonSave = (Button) v.findViewById(R.id.saveBtn);
        btnUpload = (Button) v.findViewById(R.id.btn_upload_recipe_img);
        //buttonSave.setEnabled(false);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestImage();
            }
        });


        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ing = textIn.getText().toString().trim();
                qua = quantity.getText().toString().trim();
                if(ing.isEmpty() && qua.isEmpty()){
                    textIn.setError("הזן רכיב");
                    quantity.setError("הזן כמות");
                    return;
                }
                if(!ing.isEmpty() && qua.isEmpty()){
                    quantity.setError("הזן כמות");
                    return;
                }
                if(ing.isEmpty() && !qua.isEmpty()){
                    textIn.setError("הזן רכיב");
                    return;
                }
                //check if the ingredient exist
                repo.isExistIngredient(ing, new Repository.OnAddNewIngredientListener() {
                    @Override
                    public void onSuccess(String message) {
                        if(message=="true"){
                            final View addView = layoutInflater.inflate(R.layout.row, null);
                            TextView textOut = (TextView) addView.findViewById(R.id.textout);
                            TextView textOut2 = (TextView) addView.findViewById(R.id.textout2);
                            TextView textOutType = (TextView) addView.findViewById(R.id.textout3);
                            textOut.setText(textIn.getText().toString());
                            textOut2.setText(quantity.getText().toString());
                            textOutType.setText(type);

                            ingr = new IngredientInfo(ing, Double.parseDouble(qua),type);
                            allIngredients.add(ingr);
                            textIn.setText("");
                            quantity.setText("");
                            ImageButton buttonRemove = (ImageButton) addView.findViewById(R.id.remove);
                            buttonRemove.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    ((LinearLayout) addView.getParent()).removeView(addView);
                                    for (int i = 0; i < allIngredients.size(); ++i) {
                                        if (allIngredients.get(i).getName().equals(textOut.getText().toString()))
                                            allIngredients.remove(i);
                                    }
                                }
                            });

                            container2.addView(addView);
                        }
                        else{
                            d();
//                            DialogIng dialog = new DialogIng();
//                            dialog.setTargetFragment(AddRecipeFragment.this, 1);
//                            dialog.show(getFragmentManager(), "DialogIng");
//                            Log.d("O","_________  "+dialog.getNameIng());
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openDetailsFragment(category1);
//                getFragmentManager().popBackStackImmediate();
//                Intent intent=new Intent();
//                intent.setClass(getActivity(), MainActivity.class);
//                getActivity().startActivity(intent);
                ing = textIn.getText().toString().trim();
                qua = quantity.getText().toString().trim();
                String recipesNames = getAllRecipesString();
                String nameRecipe =name.getText().toString().trim();
                if(recipesNames.contains(name.getText().toString().trim())){
                    name.setError("שם מתכון קיים ,הזן שם מתכון חדש");
                }
                if(TextUtils.isEmpty(name.getText().toString().trim())){
                    name.setError("הזן שם מתכון");
                }
                if(nameCategory.equals("קטגוריה")){
                    setSpinnerError(spinnerCategory, "בחר קטגוריה");
                }
                if(Time.equals("זמן הכנה")){
                    setSpinnerError(spinnerTime, "בחר זמן");
                }
                if(level.equals("דרגת קושי")){
                    setSpinnerError(spinnerLevel, "בחר דרגת קושי");
                }
                if(allIngredients.isEmpty()) {
                    if(ing.isEmpty() && qua.isEmpty()){
                        textIn.setError("הזן רכיב");
                        quantity.setError("הזן כמות");
                    }
                    if(!ing.isEmpty() && qua.isEmpty()){
                        quantity.setError("הזן כמות");
                    }
                    if(ing.isEmpty() && !qua.isEmpty()){
                        textIn.setError("הזן רכיב");
                    }
                }
                if(TextUtils.isEmpty(prep.getText().toString().trim())){
                    prep.setError("הזן אופן הכנה");
                }
                if(!(existRecipe == null)){
                    if(!(Time.equals("זמן הכנה"))
                            && !(level.equals("דרגת קושי"))
                            && !(nameCategory.equals("קטגוריה"))
                            && !(allIngredients.isEmpty())
                            && !(TextUtils.isEmpty(prep.getText().toString().trim()))){
                        saveInFirebase();

                    }
                }
                if(category1 != null) {
                    if (!(recipesNames.contains(name.getText().toString().trim()))
                            && !(TextUtils.isEmpty(name.getText().toString().trim()))
//                        && !(nameCategory.equals("קטגוריה"))
                            && !(Time.equals("זמן הכנה"))
                            && !(level.equals("דרגת קושי"))
                            && !(allIngredients.isEmpty())
                            && !(TextUtils.isEmpty(prep.getText().toString().trim()))) {
                        if (category1.getName().equals("כל המתכונים") && (nameCategory.equals("קטגוריה")))
                            return;
                        saveInFirebase();
                        openDetailsFragment1(category1);
                    }
                }
            }
        });
        return v;

    }

    private void requestImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),IMG_REQUEST_ID);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        if(requestCode == IMG_REQUEST_ID && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUri = data.getData();
            try {
                Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imgUri);
                imageView.setImageBitmap(bitmapImg);
                btnUpload.setEnabled(false);
//                btnAddCategory.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void saveInFirebase(){
        String Name;
        Double p = 0.0;
        if(category1 !=null) {
            if (!category1.getName().equals("כל המתכונים")) {
                nameCategory = category1.getName();
            }
            Name = name.getText().toString().trim();
        }
        else{
            Name = existRecipe.getNameRecipe();
            p = existRecipe.getPercentIng();
        }
        if(imgUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Please Wait...");
            progressDialog.show();
            FirebaseUser currentUser = FirebaseManager.currentUser;
            String user = currentUser.getUid();

            StorageReference reference = storageReference.child(user).child("recipes").child(Name + ".jpg");
            try {
                reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        storageReference = FirebaseStorage.getInstance().getReference().child(user).child("recipes").child(Name +".jpg");
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("LLLLLLLLLLLLL", "allIngredients=====" + allIngredients);
                                //String Name = name.getText().toString().trim();
                                String Prep = prep.getText().toString().trim();
                                Double p = 0.0;
                                if(existRecipe!=null)
                                    p=existRecipe.getPercentIng();
                                Recipe recipe = new Recipe(Name,nameCategory,Time,allIngredients,Prep,p,uri.toString(),level);
                                repo.saveNewRecipe(recipe, new Repository.OnAddNewRecipeListener() {
                                    @Override
                                    public void onSuccess(String message) {
                                        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                                        Log.d("saveNewRecipe::Succeed",message);
                                        if(existRecipe!=null){
                                            openDetailsFragment(recipe);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                        Log.d("saveNewRecipe::Failure",e.getLocalizedMessage());
                                    }
                                });
//                                Category category = new Category(uri.toString(),Name,0);
//                                repo.addCategory(category, new OnSuccessListener() {
//                                    @Override
//                                    public void onSuccess(Object o) {
//                                        Toast.makeText(getContext(),o.toString(),Toast.LENGTH_SHORT).show();
//                                    }
//                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                        Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error Occurred" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("saved " + (int) progress + "%");
                        btnUpload.setEnabled(true);
                        //btnAddCategory.setEnabled(false);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Log.d("LLLLLLLLLLLLL", "allIngredients=====" + allIngredients);
            //String Category = category.getText().toString().trim();
            String Prep = prep.getText().toString().trim();
            String uri= "null";
            if(existRecipe!=null) {
                p = existRecipe.getPercentIng();
                uri = existRecipe.getImgUrl();
            }
            Recipe recipe = new Recipe(Name,nameCategory,Time,allIngredients,Prep,p,uri,level);

            repo.saveNewRecipe(recipe, new Repository.OnAddNewRecipeListener() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                    Log.d("saveNewRecipe::Succeed",message);
                    if(existRecipe!=null){
                        openDetailsFragment(recipe);
                    }
                }
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.d("saveNewRecipe::Failure",e.getLocalizedMessage());
                }
            });
        }
    }
    public void addToArray(String newIng){
        Log.d("addToArray", "addToArray");
        if(myListIng!=null) {
            myListIng = Arrays.copyOf(myListIng, myListIng.length + 1);
            myListIng[myListIng.length - 1] = newIng;
            textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, myListIng));
        }
    }
    public void showFragment(Fragment frag) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.replace(R.id.nav_host_fragment, frag);
        tran.commit();
    }
    private void openDetailsFragment(Recipe recipe) {
        mListener.showFragment(R.id.nav_item_details,ItemDetailsFragment.newInstance(recipe));
    }
    private void openDetailsFragment1(Category category) {
        mListener.showFragment(R.id.listRecipesFragment2,ListRecipesFragment.newInstance(category));
    }
    @Override
    public void sendInput(String input) {
        Log.d("DDDialog", "sendInput: found incoming input: " + input);
        if(myListIng!=null) {
            myListIng = Arrays.copyOf(myListIng, myListIng.length + 1);
            myListIng[myListIng.length - 1] = input;
            textIn.setAdapter(new ArrayAdapter<>(AddRecipeFragment.this.getContext(), android.R.layout.simple_list_item_1, myListIng));
        }
        mInputDisplay.setText(input);
    }
    public void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError(error);
        }
    }
    public String getAllRecipesString() {
        repo.getAllRecipes(new Repository.OnSearchAllRecipes() {
            @Override
            public void onRecipesFound(List<Recipe> matches) {
                for(int i=0; i<matches.size(); i++)
                {
                    all += " " + matches.get(i).getNameRecipe();
                }
            }

            @Override
            public void onNoRecipesFound(String message) {

            }

            @Override
            public void onExceptionOccurred(Exception e) {

            }
        });
        return all;
    }
    public void d(){
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_spinner,null);
        alert.setTitle("מוצר חדש");

        Spinner mspinner = (Spinner) view.findViewById(R.id.spinner3);
        EditText name = (EditText) view.findViewById(R.id.name_ing);
        ArrayAdapter <String> marrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.typesList));
        marrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinner.setAdapter(marrayAdapter);

        alert.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!mspinner.getSelectedItem().toString().equals("בחר סוג")){
                    Toast.makeText(getContext(),mspinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                    Ingredient ingredient = new Ingredient(name.getText().toString().trim(), 0.0, null, mspinner.getSelectedItem().toString());
                    repo.saveNewIngredient(ingredient, new Repository.OnAddNewIngredientListener() {
                        @Override
                        public void onSuccess(String message) {
                            Log.d("saveNewIng::Succeed", message);
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("saveNewIng::Failure", e.getLocalizedMessage());
                        }
                    });
                    dialog.dismiss();
                }
            }
        });
        alert.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setView(view);
        AlertDialog dialog = alert.create();
        dialog.show();
    }
}
