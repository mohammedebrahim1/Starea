package com.example.geek.starea.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.geek.starea.Models.ModelComment;
import com.example.geek.starea.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.CommentHolder> {
    Context context;
    List<ModelComment> commentList;

    public AdapterComments(Context context, List<ModelComment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment , parent , false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        // get data
        String uid = commentList.get(position).getUid();
        String uEmail = commentList.get(position).getuEmail();
        String uName = commentList.get(position).getuName();
        String uDp = commentList.get(position).getuDp();
        String cId = commentList.get(position).getcId();
        String timeStamp = commentList.get(position).getTimeStamp();
        String comment = commentList.get(position).getComment();
        // convert timestamp
        final Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String cTime = DateFormat.format("hh:mm aa" , calendar).toString();

        // set data
        holder.uNameTv.setText(uName);
        holder.pTimeTv.setText(cTime);
        holder.commentTv.setText(comment);
        try {
            Picasso.get().load(uDp).placeholder(R.drawable.ic_default_image).into(holder.uImageIv);
        } catch (Exception e) {
            e.printStackTrace();
            Picasso.get().load(R.drawable.ic_default_image).into(holder.uImageIv);
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        // declare views
        @BindView(R.id.uImageIv)
        CircularImageView uImageIv;
        @BindView(R.id.uNameTv)
        TextView uNameTv;
        @BindView(R.id.correct_answer)
        ImageView correctAnswer;
        @BindView(R.id.pVotesTv)
        TextView pVotesTv;
        @BindView(R.id.pTimeTv)
        TextView pTimeTv;
        @BindView(R.id.voteBtn)
        ImageButton voteBtn;
        @BindView(R.id.post_text_content)
        TextView commentTv;

        public CommentHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this , itemView);
        }
    }
}
