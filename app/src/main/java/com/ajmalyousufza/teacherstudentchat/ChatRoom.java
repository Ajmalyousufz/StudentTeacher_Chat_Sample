package com.ajmalyousufza.teacherstudentchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajmalyousufza.teacherstudentchat.Adapters.MessagesAdapter;
import com.ajmalyousufza.teacherstudentchat.ModelClass.MessagesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoom extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MessagesModel> messagesModelArrayList;
    MessagesAdapter messagesAdapter;
    ImageView sendMessage;
    EditText bodyMessage;
    CircleImageView receiverImg;
    TextView receiverNam;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;

    String messageText;
    String receiverImage,receiverName,receiverUid,userType;
    String senderUid;
    String senderRoom,receiverRoom;

    public static String sImage;
    public static String rImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        receiverName = getIntent().getStringExtra("receiverName");
        receiverUid = getIntent().getStringExtra("receiverUid");
        receiverImage = getIntent().getStringExtra("receiverImage");
        userType = getIntent().getStringExtra("userType");

        auth = FirebaseAuth.getInstance();
        senderUid = auth.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerView = findViewById(R.id.recyclerview);
        messagesModelArrayList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(this,messagesModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messagesAdapter);

        sendMessage = findViewById(R.id.sendMessage);
        bodyMessage = findViewById(R.id.bodyMessage);
        receiverNam = findViewById(R.id.receiverName);
        receiverImg = findViewById(R.id.receiverImage);

        senderRoom = senderUid+receiverUid;
        receiverRoom = receiverUid+senderUid;

        receiverNam.setText(receiverName);
        Picasso.get().load(receiverImage).into(receiverImg, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });

        DatabaseReference chatRef = firebaseDatabase.getReference().child("chatRoom").child(senderRoom).child("messages");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessagesModel messagesModel = dataSnapshot.getValue(MessagesModel.class);
                    messagesModelArrayList.add(messagesModel);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(userType=="Student"){

            DatabaseReference reference = firebaseDatabase.getReference().child("user").child(userType).child(auth.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Toast.makeText(getApplicationContext(), "userType ; "+userType, Toast.LENGTH_SHORT).show();

                    sImage = snapshot.child("student_prof_img_uri").getValue().toString();
                    rImage = receiverImage;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        if(userType=="Teacher"){

            DatabaseReference reference = firebaseDatabase.getReference().child("user").child(userType).child(auth.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Toast.makeText(getApplicationContext(), "userType ; "+userType, Toast.LENGTH_SHORT).show();

                    sImage = snapshot.child("teacher_prof_img_uri").getValue().toString();
                    rImage = receiverImage;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


        sendMessage.setOnClickListener(view -> {

            if(bodyMessage.getText().toString()!=null){
                messageText = bodyMessage.getText().toString();
                bodyMessage.setText("");
                Date date = new Date();

                MessagesModel messagesModel = new MessagesModel(messageText,senderUid,date.getTime());

                firebaseDatabase.getReference().child("chatRoom")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messagesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            firebaseDatabase.getReference().child("chatRoom")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .push().setValue(messagesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }

                    }
                });

            }
            else {
                Toast.makeText(getApplicationContext(), "Enter Message then Send", Toast.LENGTH_SHORT).show();
            }

        });

    }

}