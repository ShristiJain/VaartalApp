package com.example.vartalapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.appcompat.widget.Toolbar;

public class ChoiceActivity extends AppCompatActivity {
    Button myChat;
    RadioButton classroom,business,chatGroup;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        myChat=findViewById(R.id.myChat);
        myChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent =new Intent(ChoiceActivity.this,ChatActivity.class);
            startActivity(intent);
            finish();
            }
        });
       classroom=findViewById(R.id.classroom);
       business=findViewById(R.id.business);
       chatGroup=findViewById(R.id.groupChatRB);
       classroom.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ChoiceActivity.this,ClassroomLoginActivity.class);
               startActivity(intent);
               finish();
           }
       });
       business.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent =new Intent(ChoiceActivity.this,BusinessLoginActivity.class);
               startActivity(intent);
               finish();
           }
       });
       chatGroup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent =new Intent(ChoiceActivity.this,NewGroupActivity.class);
               startActivity(intent);
               finish();
           }
       });
    }

}
