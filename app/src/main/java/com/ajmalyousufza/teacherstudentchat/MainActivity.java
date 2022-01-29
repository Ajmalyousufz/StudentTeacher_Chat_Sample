package com.ajmalyousufza.teacherstudentchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button student,teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        student = findViewById(R.id.studentBtn);
        teacher = findViewById(R.id.teacherBtn);

        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(this,TeacherChat.class));
            finish();
        }

        student.setOnClickListener(view -> {

            Intent intent = new Intent(this,StudentSignIn.class);

            intent.putExtra("userType","Student");

            startActivity(intent);

        });

        teacher.setOnClickListener(view -> {

            Intent intent = new Intent(this,RegisterUser.class);

            intent.putExtra("userType","Teacher");

            startActivity(intent);
        });

    }
}