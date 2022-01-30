package com.ajmalyousufza.teacherstudentchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    SharedPreferences sh;
    Button student,teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        sh = getSharedPreferences("userPrefence",MODE_PRIVATE);
        SharedPreferences.Editor shEditor = sh.edit();

        student = findViewById(R.id.studentBtn);
        teacher = findViewById(R.id.teacherBtn);

        if(auth.getCurrentUser()!=null){

            String userType = sh.getString("userType","");

            if(userType.equals("Teacher")){
                startActivity(new Intent(this,TeacherChat.class));
                finish();
            }else  if(userType.equals("Student")){
                startActivity(new Intent(this,StudentCaht.class));
                finish();
            }

        }

        student.setOnClickListener(view -> {

            shEditor.putString("userType","Student");
            shEditor.apply();
            shEditor.commit();

            Intent intent = new Intent(this,StudentSignIn.class);

            intent.putExtra("userType","Student");

            startActivity(intent);
            finish();

        });

        teacher.setOnClickListener(view -> {

            shEditor.putString("userType","Teacher");
            shEditor.apply();
            shEditor.commit();

            Intent intent = new Intent(this,RegisterUser.class);

            intent.putExtra("userType","Teacher");

            startActivity(intent);
            finish();
        });

    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}