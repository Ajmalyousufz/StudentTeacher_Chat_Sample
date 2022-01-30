package com.ajmalyousufza.teacherstudentchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ajmalyousufza.teacherstudentchat.Adapters.StudentRVAdapter;
import com.ajmalyousufza.teacherstudentchat.Adapters.TeacherRVAdaper;
import com.ajmalyousufza.teacherstudentchat.ModelClass.SrudentUserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllStudents extends AppCompatActivity {

    RecyclerView recyclerView;
    TeacherRVAdaper teacherRVAdaper;
    ArrayList<SrudentUserModel> srudentUserModelArrayList;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_students);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase  = FirebaseDatabase.getInstance();

        recyclerView = findViewById(R.id.recyclerview);
        srudentUserModelArrayList = new ArrayList<>();
        teacherRVAdaper = new TeacherRVAdaper(srudentUserModelArrayList,this);

        rvcaller();
        loadData();
    }

    private void loadData() {
        srudentUserModelArrayList.clear();
        DatabaseReference databaseReference1 = firebaseDatabase.getReference().child("user")
                .child("Student");
        //Query orderStatusQuery = databaseReference1.orderByChild("teacherId").equalTo(teacherUId);

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                srudentUserModelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SrudentUserModel srudentUserModel = dataSnapshot.getValue(SrudentUserModel.class);
                    srudentUserModelArrayList.add(srudentUserModel);
                    teacherRVAdaper.notifyDataSetChanged();
                }
                rvcaller();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void rvcaller() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(teacherRVAdaper);
    }
}