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

public class NewGroupActivity extends AppCompatActivity {
    Button uploadImage,create;
    EditText groupName,des;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    private static final  int galeryPick=1;
    private StorageReference imageRef;
    private String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        auth=FirebaseAuth.getInstance();
        currentUser=FirebaseAuth.getInstance().getUid();
        rootRef= FirebaseDatabase.getInstance().getReference();
        uploadImage=findViewById(R.id.group_icon_button);
        create=findViewById(R.id.create_group_button);
        groupName=findViewById(R.id.group_name_et);
        des=findViewById(R.id.group_des_et);
        imageRef= FirebaseStorage.getInstance().getReference().child("group icon images");

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageMethod();
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String group_name= groupName.getText().toString();
                String group_description=des.getText().toString();
                if(!(group_name.equalsIgnoreCase(""))){
                createNewGroup(group_name,group_description);}
                else{
                    Toast.makeText(NewGroupActivity.this,"Please enter a group name",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createNewGroup(final String group_name, String group_description) {
     rootRef.child("groups").child(group_name).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
         @Override
         public void onComplete(@NonNull Task<Void> task) {
         if(task.isSuccessful()){
             Toast.makeText(NewGroupActivity.this,group_name+"group is successfully created.",Toast.LENGTH_LONG).show();
         }
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
            if(resultCode == RESULT_OK){
                Uri resultUri=result.getUri();
                StorageReference filePath=imageRef.child(currentUser+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NewGroupActivity.this,"Image uploaded successfully.",Toast.LENGTH_SHORT).show();

                            final String downloadUrl=task.getResult().getStorage().getDownloadUrl().toString();
                            rootRef.child("users").child(currentUser).child("group icon images").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(NewGroupActivity.this,"Image added to the Database",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String error=task.getException().toString();
                                        Toast.makeText(NewGroupActivity.this,"error :  "+error,Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });



                        }
                        else{
                            String error=task.getException().toString();
                            Toast.makeText(NewGroupActivity.this,"Error :  "+error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

        }
    }


}
