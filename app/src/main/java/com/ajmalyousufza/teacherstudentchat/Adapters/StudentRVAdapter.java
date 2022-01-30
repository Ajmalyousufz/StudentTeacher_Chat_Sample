package com.ajmalyousufza.teacherstudentchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        Picasso.get().load(usersArrayList.get(position).getTeacher_prof_img_uri()).into(holder.studentImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

                Toast.makeText(context.getApplicationContext(), "Image loading error "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView studentName,studentClass,studClass;
        ImageView studentImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studentClass = itemView.findViewById(R.id.classtxt);
            studentName = itemView.findViewById(R.id.prof_name_stud);
            studentImage = itemView.findViewById(R.id.prof_image_stud);
            studClass = itemView.findViewById(R.id.prof_class_stud);
        }
    }
}
