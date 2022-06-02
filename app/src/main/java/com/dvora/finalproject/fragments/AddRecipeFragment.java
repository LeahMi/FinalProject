package com.dvora.finalproject.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dvora.finalproject.FirebaseManager;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddRecipeFragment extends Fragment implements DialogIng.OnInputSelected{
    public final static String CATEGORY_KEY = "CATEGORY_KEY";
    private ImageView imageView;
    private Category category1;
    private AutoCompleteTextView textIn;
    private EditText quantity, prep, name, category, time;
    private Button buttonAdd, buttonSave, btnUpload;
    private String qua, ing, type, level;
    private IngredientInfo ingr;
    private String[] types = {"גרם","קורט","מל","יחידה","כפית","כף","כוס"};
    private String[] levels = {"קל","בינוני","קשה"};
    private String[] myListIng;
    private List<IngredientInfo> allIngredients = new ArrayList<>();
    private TextView mInputDisplay;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private final int IMG_REQUEST_ID = 10;
    private Uri imgUri;
    private Repository repo = new Repository();


    public static AddRecipeFragment newInstance(Category category) {
        AddRecipeFragment fragment = new AddRecipeFragment();
        Bundle args = new Bundle();
        args.putSerializable(CATEGORY_KEY, category);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            Bundle bundle = getArguments();
            category1 = (Category) bundle.getSerializable(CATEGORY_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageView = (ImageView) v.findViewById(R.id.image_view_recipe_img);
        Spinner spinner =(Spinner) v.findViewById(R.id.spinner2);
        Spinner spinner1 = (Spinner) v.findViewById(R.id.spinner_level);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, levels);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                level = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        Toast.makeText(getContext(),category1.getName(),Toast.LENGTH_SHORT).show();
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
        //category = (EditText) v.findViewById(R.id.category);
        time = (EditText) v.findViewById(R.id.time);
        prep = (EditText) v.findViewById(R.id.prep);
        buttonAdd = (Button) v.findViewById(R.id.add);
        buttonSave = (Button) v.findViewById(R.id.saveBtn);
        btnUpload = (Button) v.findViewById(R.id.btn_upload_recipe_img);
        //buttonSave.setEnabled(false);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestImage();
            }
        });
        LinearLayout container2 = (LinearLayout) v.findViewById(R.id.container);


        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ing = textIn.getText().toString().trim();
                qua = quantity.getText().toString().trim();
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
                            Button buttonRemove = (Button) addView.findViewById(R.id.remove);
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
                            DialogIng dialog = new DialogIng();
                            dialog.setTargetFragment(AddRecipeFragment.this, 1);
                            dialog.show(getFragmentManager(), "DialogIng");
                            Log.d("O","_________  "+dialog.getNameIng());
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
                saveInFirebase();
                FirebaseUser currentUser = FirebaseManager.currentUser;
                String user =currentUser.getUid();
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
        if(imgUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Please Wait...");
            progressDialog.show();
            FirebaseUser currentUser = FirebaseManager.currentUser;
            String user = currentUser.getUid();
            String Name = name.getText().toString().trim();
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
                                String Name = name.getText().toString().trim();
                                String Time = time.getText().toString().trim();
                                //String Category = category.getText().toString().trim();
                                String Prep = prep.getText().toString().trim();
                                Recipe recipe = new Recipe(Name,category1.getName(),Time,allIngredients,Prep,0.0,uri.toString());
                                repo.saveNewRecipe(recipe, new Repository.OnAddNewRecipeListener() {
                                    @Override
                                    public void onSuccess(String message) {
                                        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                                        Log.d("saveNewRecipe::Succeed",message);
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
            String Name = name.getText().toString().trim();
            String Time = time.getText().toString().trim();
            //String Category = category.getText().toString().trim();
            String Prep = prep.getText().toString().trim();
            Recipe recipe = new Recipe(Name,category1.getName(),Time,allIngredients,Prep,0.0,"null");
            repo.saveNewRecipe(recipe, new Repository.OnAddNewRecipeListener() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                    Log.d("saveNewRecipe::Succeed",message);
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
        tran.replace(R.id.fragment, frag);
        tran.commit();
    }
    private void openDetailsFragment(Category category) {
        showFragment(CategoryDetailsFragment.newInstance(category));
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
}
