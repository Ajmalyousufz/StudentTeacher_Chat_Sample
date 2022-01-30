package com.ajmalyousufza.teacherstudentchat.Adapters;

import static com.ajmalyousufza.teacherstudentchat.ChatRoom.rImage;
import static com.ajmalyousufza.teacherstudentchat.ChatRoom.sImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajmalyousufza.teacherstudentchat.ModelClass.MessagesModel;
import com.ajmalyousufza.teacherstudentchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessagesModel> messagesModelArrayList;

    public MessagesAdapter(Context context, ArrayList<MessagesModel> messagesModelArrayList) {
        this.context = context;
        this.messagesModelArrayList = messagesModelArrayList;
    }

    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == ITEM_SEND){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_layout_item,parent,false);
            return new SenderViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_layout_item,parent,false);
            return new ReceiverViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessagesModel messagesModel = messagesModelArrayList.get(position);

        if(holder.getClass()==SenderViewHolder.class){

            SenderViewHolder viewHolder = (SenderViewHolder) holder;

            Picasso.get().load(sImage).into(viewHolder.profImage);
            viewHolder.txt_message.setText(messagesModel.getMessage());

        }else{
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;

            Picasso.get().load(rImage).into(viewHolder.profImage);
            viewHolder.txt_message.setText(messagesModel.getMessage());

        }

    }

    @Override
    public int getItemCount() {
        return messagesModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessagesModel messagesModel = messagesModelArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messagesModel.getSenderId())){
            return ITEM_SEND;
        }
        else {
            return ITEM_RECEIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profImage;
        TextView txt_message;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            profImage = itemView.findViewById(R.id.senderImageIcon);
            txt_message = itemView.findViewById(R.id.senderMessage);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profImage;
        TextView txt_message;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            profImage = itemView.findViewById(R.id.receiverImageIcon);
            txt_message = itemView.findViewById(R.id.receiverMessage);
        }
    }
}
