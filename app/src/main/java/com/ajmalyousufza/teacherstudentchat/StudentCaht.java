package com.ajmalyousufza.teacherstudentchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentCaht extends AppCompatActivity {

    Toolbar toolbar;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;

    RecyclerView recyclerView;
    StudentRVAdapter studentRVAdapter;
    ArrayList<Users> usersArrayList;

    TextView studentName;
    CircleImageView studentImage;

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

        studentName = findViewById(R.id.myProfileName);
        studentImage = findViewById(R.id.myProfileImage);

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

                   if(dataSnapshot.child("student_name").getKey().toString().equals("student_name")){
                       String profNam = dataSnapshot.child("student_name").getValue().toString();
                       String[] separated = profNam.split("@");
                       String studentUsernameSeperated = separated[0];
                       studentUsernameSeperated = studentUsernameSeperated.replace("_", " ");
                       studentName.setText(studentUsernameSeperated);
                   }

                   if(dataSnapshot.child("student_prof_img_uri").getKey().toString().equals("student_prof_img_uri")){
                       Picasso.get().load(dataSnapshot.child("student_prof_img_uri").getValue().toString())
                               .into(studentImage, new Callback() {
                                   @Override
                                   public void onSuccess() {

                                   }

                                   @Override
                                   public void onError(Exception e) {

                                   }
                               });
                   }

                   if(dataSnapshot.child("teacherId").getKey().toString().equals("teacherId")){

                       String teacherid = dataSnapshot.child("teacherId").getValue().toString();

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
                               Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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