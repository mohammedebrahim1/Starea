package com.example.geek.starea.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geek.starea.Chat.ChatActivity;
import com.example.geek.starea.Models.ModelUser;
import com.example.geek.starea.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.myHolder> {
    Context context;

    List<ModelUser> userList;

    public AdapterUser(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout row user xml

        View view = LayoutInflater.from(context).inflate(R.layout.row_user, parent, false);

        return new myHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int position) {
        // get data
        final String hisUid = userList.get(position).getUid();
        String userImage = userList.get(position).getPhoto();
        String userName = userList.get(position).getName();
        final String userEmail = userList.get(position).getEmail();
        // set data
        myHolder.mNameTv.setText(userName);
        myHolder.mEmailTv.setText(userEmail);
        try {
          //  Toast.makeText(context , "im " + userImage , Toast.LENGTH_LONG).show();
            Picasso.get().load(userImage).placeholder(R.drawable.ic_default_image).into(myHolder.mAvatarIv);

        } catch (Exception e) {
            e.printStackTrace();

        }
        // handle item click
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //identify user by uid
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid", hisUid);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // view holder class
    class myHolder extends RecyclerView.ViewHolder {
        ImageView mAvatarIv;
        TextView mNameTv, mEmailTv;

        public myHolder(@NonNull View context) {
            super(context);
            // init views
            mAvatarIv = context.findViewById(R.id.profile_iv);
            mNameTv = context.findViewById(R.id.name_tv);
            mEmailTv = context.findViewById(R.id.email_tv);

        }
    }
}
