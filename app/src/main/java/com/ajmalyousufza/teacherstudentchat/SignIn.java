package com.ajmalyousufza.teacherstudentchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {

    EditText username,password;
    Button signIn;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;

    String userTyp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        username =findViewById(R.id.username);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signInButton);

        signIn.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(username.getText())||!TextUtils.isEmpty(password.getText())){

                auth.signInWithEmailAndPassword(username.getText().toString()+"@aves.com"
                        ,password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Signed In Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), TeacherChat.class);
                            intent.putExtra("teacherUsername", username.getText().toString());
                            intent.putExtra("teacherUid", auth.getUid());
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Sign in Failed! User Not Registered!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else {
                Toast.makeText(getApplicationContext(), "Enter all credentials", Toast.LENGTH_SHORT).show();
            }
        });

    }
}