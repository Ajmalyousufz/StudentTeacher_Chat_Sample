package com.ajmalyousufza.teacherstudentchat;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajmalyousufza.teacherstudentchat.Adapters.TeacherRVAdaper;
import com.ajmalyousufza.teacherstudentchat.ModelClass.SrudentUserModel;
import com.ajmalyousufza.teacherstudentchat.ModelClass.Users;
import com.ajmalyousufza.teacherstudentchat.util.ImageUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.nio.channels.AsynchronousChannel;
import java.util.ArrayList;

public class TeacherChat extends AppCompatActivity {

    RecyclerView recyclerView;
    TeacherRVAdaper teacherRVAdaper;
    ArrayList<SrudentUserModel> srudentUserModelArrayList;

    FirebaseAuth auth1,auth2;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    FloatingActionButton viewFABparent,viewAllStudent_fab,addStudent_fab;

    TextView addStudenttxt,viewStudenttxt,userName;
    ImageView studImg;

    Boolean isAllFabsVisible;
    Uri profImageUri;
    Bitmap bitmap;
    String imageString="";
    String teacherUsername;
    String teacherUId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_chat);


        auth1 = FirebaseAuth.getInstance();
        teacherUId  = auth1.getCurrentUser().getUid().toString();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://teacherstudentchat-default-rtdb.firebaseio.com/")
                .setApiKey("AIzaSyBIf_4l1X6DPMBrEdb1XtsDtTpqfldwmXU")
                .setApplicationId("teacherstudentchat").build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "AnyAppName");
            auth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e){
            auth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("AnyAppName"));
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerview);
        srudentUserModelArrayList = new ArrayList<>();
        rvCaller();
        loadData();


        viewFABparent = findViewById(R.id.add_fab);
        viewAllStudent_fab = findViewById(R.id.view_all_stud_fab);
        addStudent_fab = findViewById(R.id.add_person_fab);

        addStudenttxt = findViewById(R.id.add_person_action_text);
        viewStudenttxt = findViewById(R.id.view_stud_action_text);
        userName = findViewById(R.id.userNameSigned);

        viewAllStudent_fab.setVisibility(View.GONE);
        addStudent_fab.setVisibility(View.GONE);
        addStudenttxt.setVisibility(View.GONE);
        viewStudenttxt.setVisibility(View.GONE);

        isAllFabsVisible = false;

        teacherUsername = auth1.getCurrentUser().getEmail().toString();
        String[] separated = teacherUsername.split("@");
        String teacherUsernameSeperated = separated[0];
        userName.setText(teacherUsernameSeperated);

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
            // Create an alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // set the custom layout
            final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);
            builder.setView(customLayout);

            EditText studName = customLayout.findViewById(R.id.studentName);
            EditText studPass = customLayout.findViewById(R.id.studentPass);
            EditText studClass = customLayout.findViewById(R.id.studentClass);

            studImg = customLayout.findViewById(R.id.prof_image);

            studImg.setOnClickListener(view1 -> {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                someActivityResultLauncher.launch(intent);
            });



            studImg.setImageURI(profImageUri);

            // add a button
            builder.setPositiveButton("Add",
                    new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // send data from the
                                    // AlertDialog to the Activity

                                    sendDialogDataToActivity(studName.getText().toString(),studPass.getText().toString()
                                    ,studClass.getText().toString(),imageString);
                                }
                            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }

    private void sendDialogDataToActivity(String studName,String studPass,String studClass,String profImgStr) {

        if(!studName.equals("") || !studPass.equals("")){

           String  uName = studName+"@aves.com";
            uName = uName.replace(" ", "_");
           String userType = "Student";
            final String[] profImageUri_Str = {""};

            String finalUName = uName;
            auth2.createUserWithEmailAndPassword(uName, studPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        if(auth2.getCurrentUser().getUid()!=null){

                            DatabaseReference databaseReference = firebaseDatabase.getReference().child("user")
                                    .child(userType).child(auth2.getUid());

                            StorageReference storageReference = firebaseStorage.getReference().child("upload")
                                    .child(userType).child(auth2.getUid());

                            //Uploading Image To Firebase Storage and getting link

                            if(profImageUri!=null){
                                storageReference.putFile(profImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if(task.isSuccessful()){
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    profImageUri_Str[0] = uri.toString();
                                                    SrudentUserModel users = new SrudentUserModel(auth2.getUid(), finalUName, studPass, profImageUri_Str[0],teacherUId,userType,studClass);
                                                    databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(getApplicationContext(), "New Student Account Created Successfully", Toast.LENGTH_SHORT).show();
                                                                auth2.signOut();
                                                            }
                                                            else {
                                                                Toast.makeText(getApplicationContext(), "New Student Account Creation Failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Task error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else {

                                Uri profIconUri =Uri.parse( "https://firebasestorage.googleapis.com/v0/b/teacherstudentchat.appspot.com/o/profile-user.png?alt=media&token=09dba324-0a10-4f34-b97f-5f1ed32bfba7");
                                profImageUri_Str[0] = profIconUri.toString();
                                SrudentUserModel users = new SrudentUserModel(auth2.getUid(), finalUName, studPass, profImageUri_Str[0],teacherUId,userType,studClass);
                                databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "New Student Account Created Successfully", Toast.LENGTH_SHORT).show();

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "New Student Account Creation Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                 }

                        }else {
                            Toast.makeText(getApplicationContext(), "Error user not created", Toast.LENGTH_SHORT).show();
                        }



                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Task Error or User Exists", Toast.LENGTH_SHORT).show();
                    }
                }
            });




        }
        else {
            Toast.makeText(getApplicationContext(), "Enter username and password", Toast.LENGTH_SHORT).show();
        }

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if(data!=null) {
                            Uri uri = data.getData();
                            profImageUri = uri;
                            studImg.setImageURI(uri);

                            try {
                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                                imageString = new ImageUtils().getStringImage(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacherchat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                auth1.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
        }
        return true;
    }

    public void rvCaller(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        teacherRVAdaper = new TeacherRVAdaper(srudentUserModelArrayList,TeacherChat.this);
        recyclerView.setAdapter(teacherRVAdaper);
    }

    public void loadData(){
        srudentUserModelArrayList.clear();
        DatabaseReference databaseReference1 = firebaseDatabase.getReference().child("user")
                .child("Student");
        Query orderStatusQuery = databaseReference1.orderByChild("teacherId").equalTo(teacherUId);

        orderStatusQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                srudentUserModelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SrudentUserModel srudentUserModel = dataSnapshot.getValue(SrudentUserModel.class);
                    srudentUserModelArrayList.add(srudentUserModel);
                    teacherRVAdaper.notifyDataSetChanged();
                    rvCaller();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}