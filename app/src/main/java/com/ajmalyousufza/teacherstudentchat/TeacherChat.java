package com.ajmalyousufza.teacherstudentchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TeacherChat extends AppCompatActivity {

    FloatingActionButton viewFABparent,viewAllStudent_fab,addStudent_fab;

    TextView addStudenttxt,viewStudenttxt;

    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_chat);

        viewFABparent = findViewById(R.id.add_fab);
        viewAllStudent_fab = findViewById(R.id.view_all_stud_fab);
        addStudent_fab = findViewById(R.id.add_person_fab);

        addStudenttxt = findViewById(R.id.add_person_action_text);
        viewStudenttxt = findViewById(R.id.view_stud_action_text);

        viewAllStudent_fab.setVisibility(View.GONE);
        addStudent_fab.setVisibility(View.GONE);
        addStudenttxt.setVisibility(View.GONE);
        viewStudenttxt.setVisibility(View.GONE);

        isAllFabsVisible = false;

        viewFABparent.setOnClickListener(view -> {
            if(!isAllFabsVisible){
                viewAllStudent_fab.show();
                addStudent_fab.show();
                addStudenttxt.setVisibility(View.VISIBLE);
                viewStudenttxt.setVisibility(View.VISIBLE);

                isAllFabsVisible = true;

            }
            else {
                viewAllStudent_fab.hide();
                addStudent_fab.hide();
                addStudenttxt.setVisibility(View.GONE);
                viewStudenttxt.setVisibility(View.GONE);

                isAllFabsVisible = false;
            }
        });

        viewAllStudent_fab.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "View All Students", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,ViewAllStudents.class));

        });

        addStudent_fab.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Add Students", Toast.LENGTH_SHORT).show();

        });

    }
}