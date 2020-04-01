package com.example.geek.starea.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geek.starea.Models.ModelChat;
import com.example.geek.starea.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List <ModelChat> chatList;
    String imgUrl;
    FirebaseUser fUser;

    public AdapterChat(Context context, List<ModelChat> chatList, String imgUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imgUrl = imgUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right , parent , false);
            return new MyHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left , parent , false);
            return new MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        // get data
        String message = chatList.get(position).getMessage();
        String timeStamp = chatList.get(position).getTimestamp();
        boolean flag = chatList.get(position).isGotIt();
        // convert time to dd/mm/yyyy hh:mm
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa" , calendar).toString();
        // set data
        holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);
        try {
            Picasso.get().load(imgUrl).placeholder(R.drawable.ic_default_image).into(holder.profileIv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // handle show dialog for delete message
        holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog to confirm delete message
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this message ? ");
                // delete button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(position);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.create().show();

            }
        });

        // handle message status
        if (position==chatList.size()-1){
            Log.e("Seen or not " , String.valueOf(flag));
            if (flag) {
                holder.isSeenTv.setText("Seen");
            } else {
                holder.isSeenTv.setText("Delivered");
            }
        }
        else {
            holder.isSeenTv.setVisibility(View.GONE);
        }

    }

    private void deleteMessage(int position) {
        final String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String messageTimeStamp = chatList.get(position).getTimestamp();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        Query query = databaseReference.orderByChild("timestamp").equalTo(messageTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    // delete the message for sender only
                    if(ds.child("sender").getValue().equals(myUID)){
                        //choose one of this 1 or 2
                        // 1- remove message from chat
                       // ds.getRef().removeValue();
                        // 2- set the value of message "the message was deleted
                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("message" , "This message was deleted...");
                        ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context , "Message deleted...." , Toast.LENGTH_LONG).show();
                    }
                    else {

                        Toast.makeText(context , "You can delete only your messages!" , Toast.LENGTH_LONG).show();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }

    // view holder class
    class MyHolder extends RecyclerView.ViewHolder {
        // views
        ImageView profileIv;
        TextView messageTv , timeTv , isSeenTv;
        LinearLayout messageLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            // init views
            profileIv = itemView.findViewById(R.id.profile_chatIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.isSentTv);
            messageLayout = itemView.findViewById(R.id.messageLayout);
        }
    }
}
