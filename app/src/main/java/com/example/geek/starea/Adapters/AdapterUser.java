package com.example.geek.starea.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geek.starea.Models.ModelUser;
import com.example.geek.starea.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.Holder> {
    Context context;
    List<ModelUser> userList;

    public AdapterUser(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // view holder class
    class Holder extends RecyclerView.ViewHolder {
        ImageView mAvatarIv;
        TextView mNameTv , mEmailTv;
        public Holder(@NonNull View context) {
            super(context);
            // init views
            mAvatarIv =context.findViewById(R.id.avatar_iv);
            mNameTv =context.findViewById(R.id.name_tv);
            mEmailTv =context.findViewById(R.id.email_tv);

        }
    }
}
