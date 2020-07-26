package com.example.vartalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class ClassroomLoginActivity extends AppCompatActivity {
    RadioButton teacher,student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_login);
        teacher=findViewById(R.id.teacher);
        student=findViewById(R.id.student);
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassroomLoginActivity.this,TeacherCreateClassroom.class);
                startActivity(intent);
                finish();
            }
        });
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassroomLoginActivity.this,StudentJoinClassroom.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
