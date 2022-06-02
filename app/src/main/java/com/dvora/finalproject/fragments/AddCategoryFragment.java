package com.dvora.finalproject.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dvora.finalproject.FirebaseManager;
import com.dvora.finalproject.R;
import com.dvora.finalproject.Repository;
import com.dvora.finalproject.entities.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AddCategoryFragment extends Fragment {

    private EditText nameCategory;
    private String Name;
    private Button btnAddCategory, btnUpload;
    private ImageView imageView;
    private Repository repo = new Repository();
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private final int IMG_REQUEST_ID = 10;
    private Uri imgUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_category, container, false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        nameCategory = (EditText) v.findViewById(R.id.nameCategory);
        btnAddCategory = (Button) v.findViewById(R.id.btn_add_category);
        imageView = (ImageView) v.findViewById(R.id.image_view);


        btnAddCategory.setEnabled(false);
        btnUpload = (Button) v.findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestImage();
            }
        });

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = nameCategory.getText().toString().trim();
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
                btnAddCategory.setEnabled(true);
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
            StorageReference reference = storageReference.child(user).child("categories").child(Name + ".jpg");
            try {
                reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        storageReference = FirebaseStorage.getInstance().getReference().child(user).child("categories").child(Name +".jpg");
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Category category = new Category(uri.toString(),Name,0);
                                repo.addCategory(category, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(getContext(),o.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("fff","FFFFFFFFFFFFFFFFFFFFFFF");
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
                        btnAddCategory.setEnabled(false);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}