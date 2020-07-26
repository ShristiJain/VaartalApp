package com.example.vartalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    String CurrentUser,profileName,status,image;
    Button uploadImageButton,updateProfileButton;
    EditText profileNameEt,statusEt;
    private static final int galeryPick=1;
    private StorageReference imageRef;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        auth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        CurrentUser= auth.getCurrentUser().getUid();
        uploadImageButton=findViewById(R.id.udatePictureChatButton);
        updateProfileButton=findViewById(R.id.UpdateProfileButton);
        profileNameEt=findViewById(R.id.nameEt);
        profileImageView=findViewById(R.id.imageView);
        statusEt=findViewById(R.id.statusEt);
        imageRef= FirebaseStorage.getInstance().getReference().child("profile images");
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageMethod();
            }
        });
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileName=profileNameEt.getText().toString();
                status=statusEt.getText().toString();

                final HashMap<String,String> profileMap=new HashMap<>();
                profileMap.put("uid",CurrentUser);
                profileMap.put("name",profileName);
                profileMap.put("status",status);
                rootRef.child("users").child(CurrentUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && snapshot.hasChild("profile images")){
                            image=snapshot.child("profile images").getValue().toString();
                            profileMap.put("profile images",image);
                            Picasso.get().load(image).into(profileImageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(!(image.equalsIgnoreCase(""))){
                profileMap.put("profile images",image);}
                rootRef.child("users").child(CurrentUser).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent=new Intent(SettingsActivity.this,ChatActivity.class);
                            startActivity(intent);
                            Toast.makeText(SettingsActivity.this,"Your profile is successfully updated.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String error= task.getException().toString();
                            Toast.makeText(SettingsActivity.this,"Error : "+error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        rootRef.child("users").child(CurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            profileName= snapshot.child("name").getValue().toString();
            status=snapshot.child("status").getValue().toString();
            profileNameEt.setText(profileName);
            statusEt.setText(status);
            if(snapshot.exists() && snapshot.hasChild("profile images")){
           image=snapshot.child("profile images").getValue().toString();
                Picasso.get().load(image).into(profileImageView);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                final StorageReference filePath=imageRef.child(CurrentUser+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this,"Image uploaded successfully.",Toast.LENGTH_SHORT).show();
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl=uri.toString();

                             //downloadUrl=task.getResult().getStorage().getDownloadUrl().toString();
                            rootRef.child("users").child(CurrentUser).child("profile images").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SettingsActivity.this,"Image added to the Database",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String error=task.getException().toString();
                                        Toast.makeText(SettingsActivity.this,"error :  "+error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                                }
                            });




                        }
                        else{
                            String error=task.getException().toString();
                            Toast.makeText(SettingsActivity.this,"Error :  "+error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }


        }
    }
}
