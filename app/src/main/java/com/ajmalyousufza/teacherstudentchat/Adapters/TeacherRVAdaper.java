package com.ajmalyousufza.teacherstudentchat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajmalyousufza.teacherstudentchat.ChatRoom;
import com.ajmalyousufza.teacherstudentchat.ModelClass.SrudentUserModel;
import com.ajmalyousufza.teacherstudentchat.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherRVAdaper extends RecyclerView.Adapter<TeacherRVAdaper.ViewHolder> {

    ArrayList<SrudentUserModel> srudentUserModelslist;
    Context context;

    public TeacherRVAdaper(ArrayList<SrudentUserModel> srudentUserModelslist, Context context) {
        this.srudentUserModelslist = srudentUserModelslist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String profNam = srudentUserModelslist.get(position).getStudent_name();
        String[] separated = profNam.split("@");
        String teacherUsernameSeperated = separated[0];
        teacherUsernameSeperated = teacherUsernameSeperated.replace("_", " ");
        holder.prof_Name.setText(teacherUsernameSeperated);
        holder.prof_Class.setText(srudentUserModelslist.get(position).getStudent_class());
        Picasso.get().load(srudentUserModelslist.get(position).getStudent_prof_img_uri()).into(holder.prof_Image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });

        String finalTeacherUsernameSeperated = teacherUsernameSeperated;
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatRoom.class);
            intent.putExtra("receiverName", finalTeacherUsernameSeperated);
            intent.putExtra("receiverImage",srudentUserModelslist.get(position).getStudent_prof_img_uri());
            intent.putExtra("receiverUid",srudentUserModelslist.get(position).getStudent_id());
            intent.putExtra("userType","Teacher");
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return srudentUserModelslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView prof_Image;
        TextView prof_Name,prof_Class;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prof_Image = itemView.findViewById(R.id.prof_image_stud);
            prof_Name = itemView.findViewById(R.id.prof_name_stud);
            prof_Class = itemView.findViewById(R.id.prof_class_stud);
        }
    }
}
