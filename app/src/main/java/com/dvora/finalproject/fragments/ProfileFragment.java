package com.dvora.finalproject.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.work.Data;

import com.dvora.finalproject.FirebaseManager;
import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.activities.Login;
import com.dvora.finalproject.activities.MainActivity;
import com.dvora.finalproject.entities.Category;
import com.dvora.finalproject.entities.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.dvora.finalproject.FirebaseManager.currentUser;


public class ProfileFragment extends BaseFragment {
    private Repository repo = new Repository();
    private TextView Name;
    private  TextView text;

    private TextView Mail;
    private Button logOut;
    private String[] types ;
//    private TextView tv_c;
    private String favorite_c;
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

        return v;
    }
}