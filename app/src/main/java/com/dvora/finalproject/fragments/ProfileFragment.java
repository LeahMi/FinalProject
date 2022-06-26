package com.dvora.finalproject.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.dvora.finalproject.FirebaseManager;
import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.activities.Login;
import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.entities.Category;
import com.dvora.finalproject.entities.Ingredient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProfileFragment extends BaseFragment {
    private Repository repo = new Repository();
    private TextView text;

    private TextView Mail,Name, selected;
    private Button logOut;
    private String[] types ;
//    private TextView tv_c;
    private String favorite_c;
    private TextView mItemSelected;
    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();
//    boolean[] selectC;
//    ArrayList<Integer> cList= new ArrayList<>();
//    String[] cArray ;

    public ProfileFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        logOut = (Button) v.findViewById(R.id.log_out);
        text=v.findViewById(R.id.favorita_c);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
            }
        });
        Spinner spinner =(Spinner) v.findViewById(R.id.spinner_category_favorite);
        repo.getAllCategories(new Repository.OnSearchAllCategories(){

            @Override
            public void onCategoriesFound(List<Category> matches) {
                types= new String[matches.size()];
                for(int i=0;i<matches.size();++i)
                {
                    types[i]=matches.get(i).getName();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,types );
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.v("item", (String) parent.getItemAtPosition(position));
                        favorite_c = (String)parent.getItemAtPosition(position);
                        repo.SaveFavoriteCategory(favorite_c);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
                repo.getFavoriteCategory(new Repository.OnSearchFavoriteCategory() {
                    @Override
                    public void onSuccess(String message) {
                        int i= Arrays.asList(types).indexOf(message);
                        Log.d("aaa","iaaa  types "   +types.length);
                        spinner.setSelection(i);
                        Log.d("aaa","iaaa   "   +i);
                    }
                    @Override
                    public void onFailure(Exception e) { }
                });

            }

            @Override
            public void onNoCategoriesFound(String message) { }
            @Override
            public void onExceptionOccurred(Exception e) { }

        });
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item , types );
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.v("item", (String) parent.getItemAtPosition(position));
//                favorite_c = (String)parent.getItemAtPosition(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) { }
//        });

//        repo.getAllCategories(new Repository.OnSearchAllCategories() {
//            @Override
//            public void onCategoriesFound(List<Category> matches) {
//
//                cArray= new String[matches.size()];
//                selectC = new boolean[cArray.length];
//                for(int i=0;i<matches.size();++i)
//                {
//                    cArray[i]=matches.get(i).getName();
//                }
//            }
//
//            @Override
//            public void onNoCategoriesFound(String message) {
//
//            }
//
//            @Override
//            public void onExceptionOccurred(Exception e) {
//
//            }
//        });
//
//
//        tv_c = v.findViewById(R.id.tv_name_c);
//        StringBuilder stringBuilder = new StringBuilder();
//        tv_c.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new  AlertDialog.Builder(
//
//                        getContext()
//                );
//                builder.setTitle("קטגוריה אהובה");
//                builder.setCancelable(false);
//                builder.setMultiChoiceItems(cArray, selectC, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
//
//                      if (b){
//                          cList.add(i);
//                          Collections.sort(cList);
//
//                      }else {
////                          if(cList.size()==1)
////                          {
//
//                              selectC[i]=false;
//                              cList.clear();
//                              stringBuilder.delete(cList.get(i),cList.get(i));
//
//
////                          }
////                          else cList.remove(i);
//                      }
//                    }
//                });
//
//                builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
////                        StringBuilder stringBuilder = new StringBuilder();
//                        for(int j=0;j<cList.size();j++)
//                        {
//                            stringBuilder.append(cArray[cList.get(j)]);
//
//                            if(j!=cList.size()-1){
//
//                                stringBuilder.append(", ");
//
//                            }
//                        }
//
//                        tv_c.setText(stringBuilder.toString());
//
//                    }
//                });
//
//                builder.setNegativeButton("בטל", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        dialogInterface.dismiss();
//
//                    }
//                });
//
//                builder.setNeutralButton("נקה הכל", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        for(int j=0; j<selectC.length;++j){
//                            selectC[j]=false;
//                            cList.clear();
//                            tv_c.setText("");
//                        }
//
//                    }
//                });
//
//                builder.show();
//
//
//            }
//        });

//        public void updateUI(FirebaseUser currentCategory){
//            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(currentCategory.getUid());
//            Log.d("TAG","currentCategory=========="+currentCategory);
//            String keyId=currentCategory.getUid();
//            Log.d("TAG","keyId=========="+keyId);
//            String in="0";
//            ref.child("favoritecategory").setValue(favorite_c);
//
////        ref.child(keyId).child("Details").setValue(currentUser);
//        }
        Name= (TextView) v.findViewById(R.id.texthello);

        repo.getProfile(new Repository.OnSearchProfile() {
            @Override
            public void onSuccess(String message) {
                Name.setText("שלום " + message);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        Mail = v.findViewById(R.id.mail);
        Mail.setText( FirebaseManager.currentUser.getEmail());

        // Allergens select
        mItemSelected = (TextView) v.findViewById(R.id.tvItemSelected);
        selected = (TextView) v.findViewById(R.id.selected);
        System.out.println("FirebaseManager.setAllergens()--- "+ FirebaseManager.allergens);
        if(FirebaseManager.allergens!=null) {
            selected.setText(concatStrings(FirebaseManager.allergens));
        }else {
            selected.setText("");
            selected.setHint("לבחירת אלרגנים לחץ כאן");
        }
            //listItems = getResources().getStringArray(R.array.shopping_item);
//        checkedItems = new boolean[listItems.length];
            repo.getAllIngredients(new Repository.OnSearchAllIngredients() {
                @Override
                public void onIngredientsFound(List<Ingredient> matches) {
                    listItems = new String[matches.size()];
                    for (int i = 0; i < matches.size(); ++i) {
                        listItems[i] = matches.get(i).getName();
                    }
                    checkedItems = new boolean[listItems.length];
                    selected.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                            mBuilder.setTitle(R.string.dialog_title);
                            mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                                    if (isChecked) {
                                        mUserItems.add(position);
                                    } else {
                                        mUserItems.remove((Integer.valueOf(position)));
                                    }
                                }
                            });

                            mBuilder.setCancelable(false);
                            mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    String item = "";
                                    List<String> allergens = new ArrayList<String>(mUserItems.size());
                                    for (int i = 0; i < mUserItems.size(); i++) {
                                        item = item + listItems[mUserItems.get(i)];
                                        if (i != mUserItems.size() - 1) {
                                            item = item + ", ";
                                        }
                                        allergens.add(listItems[mUserItems.get(i)]);
                                    }
                                    selected.setText(item);
                                    repo.setAllergens(allergens);
                                }
                            });

                            mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    for (int i = 0; i < checkedItems.length; i++) {
                                        checkedItems[i] = false;
                                        mUserItems.clear();
                                        //mItemSelected.setText("");
                                    }
                                }
                            });

                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();
                        }
                    });
                }

                @Override
                public void onNoIngredientsFound(String message) {
                    selected.setText("הוסף מרכיבים כדי לבחור אלרגנים");
                }

                @Override
                public void onExceptionOccurred(Exception e) {
                }
            });


        return v;
    }
    public static String concatStrings(List<String> strings)
    {
        StringBuilder sb = new StringBuilder();
        int i= 0;
        for(String s: strings)
        {
            if (i == strings.size()-1 ) {
                sb.append(s);
            }
            else{sb.append(s+", ");}
            ++i;
        }
        return sb.toString();
    }
}