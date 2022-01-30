package com.ajmalyousufza.teacherstudentchat;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajmalyousufza.teacherstudentchat.ModelClass.Users;
import com.ajmalyousufza.teacherstudentchat.util.ImageUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class RegisterUser extends AppCompatActivity {

    ImageView imageView;
    EditText username,password;
    Button signUp;
    TextView gotosign_in;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage fireBaseStorage;

    String userType;
    Bitmap bitmap;
    String imageString="";
    String uName="";
    String uPass="";
    Uri profImageUri=null;
    String profImageUri_Str="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        userType = getIntent().getStringExtra("userType");

        imageView = findViewById(R.id.imageview);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signUp = findViewById(R.id.signInButton);
        gotosign_in = findViewById(R.id.alreadyuser);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fireBaseStorage = FirebaseStorage.getInstance();

        gotosign_in.setOnClickListener(view -> {
            startActivity(new Intent(this,SignIn.class));
            finish();
        });

        imageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            someActivityResultLauncher.launch(intent);
        });

        signUp.setOnClickListener(view -> {

            if(username.getText()!=null||password.getText()!=null){
                uName = username.getText().toString()+"@aves.com";
                uPass = password.getText().toString();


                auth.createUserWithEmailAndPassword(uName,uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(auth.getCurrentUser().getUid()!=null){

                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("user")
                                        .child(userType).child(auth.getUid());

                                StorageReference storageReference = fireBaseStorage.getReference().child("upload")
                                        .child(userType).child(auth.getUid());

                                //Uploading Image To Firebase Storage and getting link

                                if(profImageUri!=null){
                                    storageReference.putFile(profImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        profImageUri_Str = uri.toString();
                                                        Users users = new Users(auth.getUid(),uName,uPass,profImageUri_Str,userType);
                                                        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(getApplicationContext(), "New Teacher Account Created Successfully\nName : "+uName, Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(getApplicationContext(),TeacherChat.class);
                                                                    intent.putExtra("teacherUsername",uName);
                                                                    intent.putExtra("teacherUid",auth.getUid());
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                                else {
                                                                    Toast.makeText(getApplicationContext(), "New Teacher Account Creation Failed", Toast.LENGTH_SHORT).show();
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
                                                Uri profIconUri =Uri.parse( "https://firebasestorage.googleapis.com/v0/b/teacherstudentchat.appspot.com/o/profile-user.png?alt=media&token=09dba324-0a10-4f34-b97f-5f1ed32bfba7");
                                                profImageUri_Str = profIconUri.toString();
                                                Users users = new Users(auth.getUid(),uName,uPass,profImageUri_Str,userType);
                                                databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(getApplicationContext(), "New Teacher Account Created Successfully", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getApplicationContext(),TeacherChat.class);
                                                            intent.putExtra("teacherUsername",uName);
                                                            intent.putExtra("teacherUid",auth.getUid());
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                        else {
                                                            Toast.makeText(getApplicationContext(), "New Teacher Account Creation Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Uplaod image!", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(getApplicationContext(), "error user not created", Toast.LENGTH_SHORT).show();
                            }



                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Task Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
            else {
                Toast.makeText(getApplicationContext(), "Enter username and password", Toast.LENGTH_SHORT).show();
            }

        });

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
                            imageView.setImageURI(data.getData());
                            Uri uri = data.getData();
                            profImageUri = uri;

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


}