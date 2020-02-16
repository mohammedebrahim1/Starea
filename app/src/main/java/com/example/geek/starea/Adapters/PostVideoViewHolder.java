package com.example.geek.starea.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geek.starea.Models.PostVideoItem;
import com.example.geek.starea.Models.TimelineItem;
import com.example.geek.starea.R;

import androidx.annotation.NonNull;

public class PostVideoViewHolder extends BaseViewHolder {

    private TextView txtTime;
    private ImageView imgUser;
    public PostVideoViewHolder(@NonNull View itemView) {
        super(itemView);
        txtTime = itemView.findViewById(R.id.post_video_time);
        imgUser = itemView.findViewById(R.id.post_video_img);
    }

    @Override
    void setData(TimelineItem item) {
        PostVideoItem post = item.getPostVideoItem();
        txtTime.setText(post.getTime());
        Glide.with(super.itemView.getContext()).load(post.getImgUser()).into(imgUser);

    }
}
