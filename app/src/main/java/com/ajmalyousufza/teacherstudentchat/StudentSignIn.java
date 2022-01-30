package com.ajmalyousufza.teacherstudentchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StudentSignIn extends AppCompatActivity {

    EditText userName,passWord;
    ProgressBar progressBar;
    Button signInBtn;

    FirebaseAuth auth;

    String userNameStr,userPassStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_in);

        auth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.username);
        passWord = findViewById(R.id.password);
        signInBtn =findViewById(R.id.signInButton);
        progressBar = findViewById(R.id.progressbar);

        signInBtn.setOnClickListener(view -> {

            progressBar.setVisibility(View.VISIBLE);

            if(userName.getText().toString()!=null || passWord.getText().toString()!=null){

                userNameStr = userName.getText().toString()+"@aves.com";
                userNameStr = userNameStr.replace(" ","_");
                userPassStr = passWord.getText().toString();

                auth.signInWithEmailAndPassword(userNameStr,userPassStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Signed in Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),StudentCaht.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Signed in Failed user not exist. Check UserName "+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }else {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Enter user credentials", Toast.LENGTH_SHORT).show();

            }


        });

    }

}