package com.ajmalyousufza.teacherstudentchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ajmalyousufza.teacherstudentchat.Adapters.StudentRVAdapter;
import com.ajmalyousufza.teacherstudentchat.ModelClass.SrudentUserModel;
import com.ajmalyousufza.teacherstudentchat.ModelClass.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentCaht extends AppCompatActivity {

    Toolbar toolbar;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;

    RecyclerView recyclerView;
    StudentRVAdapter studentRVAdapter;
    ArrayList<Users> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_caht);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        usersArrayList = new ArrayList<>();
        studentRVAdapter = new StudentRVAdapter(usersArrayList,this);
        rvcaller();

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        loadData();
    }

    private void loadData() {

        DatabaseReference databaseReference2 = firebaseDatabase.getReference().child("user")
                .child("Student");
        Query orderStatusQuery = databaseReference2.orderByChild("student_id").equalTo(auth.getUid());

        orderStatusQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String teacherId = snapshot.getKey().toString();
               for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                   if(dataSnapshot.child("teacherId").getKey().toString().equals("teacherId")){

                       String teacherid = dataSnapshot.child("teacherId").getValue().toString();
                       Toast.makeText(getApplicationContext(), teacherId, Toast.LENGTH_SHORT).show();

                       DatabaseReference databaseReference3 = firebaseDatabase.getReference().child("user")
                               .child("Teacher");
                       Query orderStatusQuery1 = databaseReference3.orderByChild("teacher_id").equalTo(teacherid);

                       orderStatusQuery1.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {

                               for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                                   Users users = dataSnapshot.getValue(Users.class);
                                   usersArrayList.add(users);
                                   studentRVAdapter.notifyDataSetChanged();
                                   rvcaller();

                               }

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });

                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void rvcaller() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentRVAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacherchat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
        }
        return true;
    }
}