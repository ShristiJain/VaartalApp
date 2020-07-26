package com.example.vartalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class TeacherCreateClassroom extends AppCompatActivity {
    RadioButton create , exist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_classroom);
        create=findViewById(R.id.createNew);
        exist=findViewById(R.id.existJoin);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherCreateClassroom.this,ClassroomCreateActivity.class);
                startActivity(intent);

            }
        });
        exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(TeacherCreateClassroom.this,TeacherJoinActivity.class);
                startActivity(intent);
            }
        });
    }
}
