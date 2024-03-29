package com.example.geek.starea.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geek.starea.Models.PostTextItem;
import com.example.geek.starea.Models.TimelineItem;
import com.example.geek.starea.R;

import androidx.annotation.NonNull;

public class PostTextViewHolder extends BaseViewHolder {

    private TextView txtPost,txtTime;
    private ImageView imgUser;


    public PostTextViewHolder(@NonNull View itemView) {
        super(itemView);

        txtPost = itemView.findViewById(R.id.post_text_content);
        txtTime = itemView.findViewById(R.id.post_text_time);
        imgUser = itemView.findViewById(R.id.uImageIv);
    }

    @Override
    void setData(TimelineItem item) {
        PostTextItem post = item.getPostTextItem();
        txtPost.setText(post.getPostText());
        txtTime.setText(post.getTime());
        Glide.with(super.itemView.getContext()).load(post.getImgUser()).into(imgUser);
    }
}
