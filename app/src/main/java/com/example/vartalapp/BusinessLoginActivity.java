package com.example.vartalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class BusinessLoginActivity extends AppCompatActivity {
    private EditText businessNameET,passwordET,businessdesET;
    private Button uploadImageButton , createButton;
    private ImageView businessImage;
    private static final int galeryPick=1;
    private StorageReference imageRef;
    private FirebaseAuth auth;
    private String currentUser;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_login);
        businessNameET=findViewById(R.id.businessNameEt);
        businessdesET=findViewById(R.id.businessDespEt);
        passwordET=findViewById(R.id.businessPasswordEt);
        uploadImageButton =findViewById(R.id.uploadButton);
        createButton=findViewById(R.id.createBusinessEt);
        businessImage=findViewById(R.id.imageView2);
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser().getUid();
        rootRef= FirebaseDatabase.getInstance().getReference();
        imageRef= FirebaseStorage.getInstance().getReference().child("business icon images");
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageMethod();
            }
        });
    }

    private void uploadImageMethod() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,galeryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==galeryPick &&  resultCode==RESULT_OK &&  data!=null){
            Uri imageUri=data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode==RESULT_OK){
                Uri resultUri=result.getUri();
                StorageReference filePath=imageRef.child(currentUser+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(BusinessLoginActivity.this,"Image uploaded successfully.",Toast.LENGTH_SHORT).show();

                            final String downloadUrl=task.getResult().getStorage().getDownloadUrl().toString();
                            rootRef.child("users").child(currentUser).child("business icon images").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){
                                   Toast.makeText(BusinessLoginActivity.this,"Image added to the Database",Toast.LENGTH_SHORT).show();
                               }
                               else {
                                   String error=task.getException().toString();
                                   Toast.makeText(BusinessLoginActivity.this,"error :  "+error,Toast.LENGTH_SHORT).show();
                               }


                                }
                            });


                        }
                        else{
                            String error=task.getException().toString();
                            Toast.makeText(BusinessLoginActivity.this,"Error :  "+error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

        }
    }
}
