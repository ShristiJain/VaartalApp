package com.example.vartalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
        Button btnGenerateOTP,btnRegister;
        EditText etProfileName, etEmailId , etPhoneNumber, etOtP;
        String profileName, emailId, phoneNumber, otp;
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
       private FirebaseAuth auth;
        private String verificationCode;
       private SharedPreferences preferences;
       private DatabaseReference myRef;
       private String ProfileName,Email;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGenerateOTP=findViewById(R.id.generateOTP);
        btnRegister=findViewById(R.id.register);
        etProfileName=findViewById(R.id.profileName);
        etEmailId= findViewById(R.id.emailId);
        etPhoneNumber=findViewById(R.id.phoneNumber);
        etOtP=findViewById(R.id.otp);
        preferences= getSharedPreferences("login",MODE_PRIVATE);
        StartFirebaseLogin();
        myRef= FirebaseDatabase.getInstance().getReference();

        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProfileName=etProfileName.getText().toString();
                    if(!(ProfileName.equalsIgnoreCase(""))){
                        phoneNumber=etPhoneNumber.getText().toString();
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,
                                120,
                                TimeUnit.SECONDS,
                                MainActivity.this,
                                mCallback);
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Profile Name cannot be Empty",Toast.LENGTH_LONG).show();
                    }
                }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        otp=etOtP.getText().toString();
                        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationCode,otp);
                        SignWithPhone(credential);
                }
        });
}
        private void SignWithPhone(PhoneAuthCredential credential){
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    String CurrentUserId=auth.getCurrentUser().getUid();
                                    myRef.child("users").child(CurrentUserId).setValue("null");
                                    ProfileName= etProfileName.getText().toString();
                                    Email=etEmailId.getText().toString();
                                    HashMap<String,String> profileMap =new HashMap<>();
                                    profileMap.put("name",ProfileName);
                                    profileMap.put("status","Life is good with VaartalApp");
                                    myRef.child("users").child(CurrentUserId).setValue(profileMap);
                                        Intent intent = new Intent(MainActivity.this,ChoiceActivity.class);
                                        startActivity(intent);
                                        preferences.edit().putBoolean("login key",false).apply();
                                        finish();
                                }
                                else
                                        Toast.makeText(MainActivity.this,"INCORRECT OTP",Toast.LENGTH_LONG).show();
                        }
                });
        }
        private void StartFirebaseLogin() {
                auth = FirebaseAuth.getInstance();
                mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(MainActivity.this,"Verification completed",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                       Toast.makeText(MainActivity.this,"Verification failed",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCodeSent(String s,PhoneAuthProvider.ForceResendingToken forceResendingToken){
                                super.onCodeSent(s,forceResendingToken);
                                verificationCode=s;
                                Toast.makeText(MainActivity.this,"Code Sent",Toast.LENGTH_SHORT).show();
                        }
                };
        }
}

