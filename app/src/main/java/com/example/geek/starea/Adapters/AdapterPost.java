package com.example.geek.starea.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geek.starea.Models.ModelPost;
import com.example.geek.starea.R;
import com.example.geek.starea.ThereProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.PostHolder> {
    Context context ;
    List<ModelPost> postList ;

    public AdapterPost(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout item_post .xml
        View view  = LayoutInflater.from(context).inflate(R.layout.item_post , parent , false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        // get data
        final String uid  = postList.get(position).getUid();
        String uEmail = postList.get(position).getuEmail();
        String uName  = postList.get(position).getuName();
        String uDp  = postList.get(position).getuDp();
        String pId  = postList.get(position).getpId();
        String pContent  = postList.get(position).getpContent();
        String pImage  = postList.get(position).getpImage();
        String pTimestamp  = postList.get(position).getpTime();
        // convert timestamp
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(pTimestamp));
        String pTime = DateFormat.format("hh:mm aa" , calendar).toString();
        // set data
        holder.uNameTv.setText(uName);
        holder.pContentTv.setText(pContent);
        holder.pTimeTv.setText(pTime);
        // set user dp
        try {
            Picasso.get().load(uDp).placeholder(R.drawable.ic_default_image).into(holder.uImageIv);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // in case post has no image
        if (pImage.equals("no Image")){
            holder.pImageIv.setVisibility(View.GONE);

        }
        else {
            try {
                Picasso.get().load(pImage).placeholder(R.drawable.post_home_deafault_img).into(holder.pImageIv);
                holder.pImageIv.setVisibility(View.VISIBLE);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

        // handle buttons clicks
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "More ... " , Toast.LENGTH_LONG).show();
            }
        });
        holder.rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, " ...Rate " , Toast.LENGTH_LONG).show();
            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Comment... " , Toast.LENGTH_LONG).show();
            }
        });
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Share... " , Toast.LENGTH_LONG).show();
            }
        });
        holder.uNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on click go to clicked user profile and show his data
                Intent intent = new Intent(context , ThereProfileActivity.class);
                intent.putExtra("uid" , uid);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    // view holder class
    class PostHolder extends RecyclerView.ViewHolder{
        // views in recycle item xml
        ImageView uImageIv , pImageIv ;
        TextView uNameTv , pContentTv , pTimeTv ;
        ImageButton moreBtn;
        Button rateBtn, commentBtn , shareBtn;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            // init views
            uImageIv = itemView.findViewById(R.id.post_home_user_img);
            uNameTv = itemView.findViewById(R.id.post_home_user_name);
            pImageIv = itemView.findViewById(R.id.post_home_image);
            pContentTv = itemView.findViewById(R.id.post_text_content);
            pTimeTv = itemView.findViewById(R.id.post_text_time);
            moreBtn = itemView.findViewById(R.id.post_more);
            rateBtn = itemView.findViewById(R.id.post_rate);
            commentBtn = itemView.findViewById(R.id.post_comment);
            shareBtn = itemView.findViewById(R.id.post_share);

        }
    }
}
