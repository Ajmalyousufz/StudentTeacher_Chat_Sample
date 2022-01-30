package com.ajmalyousufza.teacherstudentchat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajmalyousufza.teacherstudentchat.ChatRoom;
import com.ajmalyousufza.teacherstudentchat.ModelClass.Users;
import com.ajmalyousufza.teacherstudentchat.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StudentRVAdapter extends RecyclerView.Adapter<StudentRVAdapter.ViewHolder> {

    ArrayList<Users> usersArrayList;
    Context context;

    public StudentRVAdapter(ArrayList<Users> usersArrayList, Context context) {
        this.usersArrayList = usersArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String studName = usersArrayList.get(position).getTeacher_name();
        String[] separated = studName.split("@");
        String studentUsernameSeperated = separated[0];
        studentUsernameSeperated = studentUsernameSeperated.replace("_", " ");
        holder.studentName.setText(studentUsernameSeperated);
        holder.studentClass.setVisibility(View.GONE);
        holder.studClass.setVisibility(View.GONE);
        holder.teach_label.setVisibility(View.VISIBLE);
        Picasso.get().load(usersArrayList.get(position).getTeacher_prof_img_uri()).into(holder.studentImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

                Toast.makeText(context.getApplicationContext(), "Image loading error "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        String finalStudentUsernameSeperated = studentUsernameSeperated;
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatRoom.class);
            intent.putExtra("receiverName", finalStudentUsernameSeperated);
            intent.putExtra("receiverImage",usersArrayList.get(position).getTeacher_prof_img_uri());
            intent.putExtra("receiverUid",usersArrayList.get(position).getTeacher_id());
            intent.putExtra("userType","Student");
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView studentName,studentClass,studClass,teach_label;
        ImageView studentImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studentClass = itemView.findViewById(R.id.classtxt);
            studentName = itemView.findViewById(R.id.prof_name_stud);
            studentImage = itemView.findViewById(R.id.prof_image_stud);
            studClass = itemView.findViewById(R.id.prof_class_stud);
            teach_label = itemView.findViewById(R.id.teacher_label);
        }
    }
}
