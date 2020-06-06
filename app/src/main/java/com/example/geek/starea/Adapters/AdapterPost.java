package com.example.geek.starea.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.geek.starea.Models.ModelPost;
import com.example.geek.starea.R;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.Calendar;
import java.util.Locale;

public class AdapterPost extends ListAdapter<ModelPost, AdapterPost.PostHolder> {

    private HomePostListeners homePostListeners;

    public AdapterPost() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout item_post .xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostHolder holder, final int position) {
        holder.bind(getItem(position));
    }


    // view holder class
    class PostHolder extends RecyclerView.ViewHolder {
        // views in recycle item xml
        ImageView uImageIv, pImageIv;
        TextView uNameTv, pContentTv, pTimeTv;
        ImageButton moreBtn;
        Button rateBtn, commentBtn, shareBtn;

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

            uNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homePostListeners.onUserClicked(getItem(getAdapterPosition()));
                }
            });
            rateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homePostListeners.onRateClicked(getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
            pImageIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bitmap = ((BitmapDrawable) pImageIv.getDrawable()).getBitmap();
                    Bitmap[] bitmaps = {bitmap};
                    new StfalconImageViewer.Builder<>(v.getContext(), bitmaps, new ImageLoader<Bitmap>() {
                        @Override
                        public void loadImage(ImageView imageView, Bitmap imageDrawable) {
                            Glide.with(imageView.getContext()).load(imageDrawable).into(imageView);
                        }
                    })
                            .withTransitionFrom(pImageIv)
                            .show();
                    homePostListeners.onImageClicked(getItem(getAdapterPosition()));
                }
            });
            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homePostListeners.onCommentClicked(getItem(getAdapterPosition()));
                }
            });
            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homePostListeners.onShareClicked(getItem(getAdapterPosition()));
                }
            });
            pContentTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homePostListeners.onContentClicked(getItem(getAdapterPosition()));
                }
            });
            moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getItem(getAdapterPosition()).isOwner()) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(), moreBtn, Gravity.END);
                        popupMenu.inflate(R.menu.post_items);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (item.getItemId() == R.id.deletePost) {
                                    homePostListeners.onDeleteClicked(getItem(getAdapterPosition()));
                                    return true;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    } else {
                        moreBtn.setVisibility(View.GONE);
                    }
                }
            });
        }

        @SuppressLint("SetTextI18n")
        void bind(ModelPost modelPost) {
            // convert timestamp
            final Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis(Long.parseLong(modelPost.getpTime()));
            // set data
            uNameTv.setText(modelPost.getuName());
            pContentTv.setText(modelPost.getpContent());
            pTimeTv.setText(DateFormat.format("hh:mm aa", calendar).toString());
            rateBtn.setText(" " + modelPost.getpRates());
            commentBtn.setText(" " + modelPost.getpComments());
            // set rates for each post
            if (modelPost.isRated())
                rateBtn.setCompoundDrawablesRelativeWithIntrinsicBounds
                        (R.drawable.ic_full_star_black, 0, 0, 0);
            else rateBtn.setCompoundDrawablesRelativeWithIntrinsicBounds
                    (R.drawable.ic_star_border_black_24dp, 0, 0, 0);
            if (modelPost.isOwner())
                moreBtn.setVisibility(View.VISIBLE);
            else moreBtn.setVisibility(View.GONE);
            // set user dp
//            Picasso.get().load(modelPost.getuDp()).placeholder(R.drawable.ic_default_image).fit().into(uImageIv);
            Glide.with(uImageIv.getContext()).load(modelPost.getuDp()).placeholder(R.drawable.ic_default_image).into(uImageIv);
            // in case post has no image
            if (modelPost.getpImage().equals("no Image")) {
                pImageIv.setVisibility(View.GONE);
            } else {
                pImageIv.setVisibility(View.VISIBLE);
//                Picasso.get().load(modelPost.getpImage()).placeholder(R.drawable.post_home_deafault_img).fit().into(pImageIv);
                Glide.with(pImageIv.getContext()).load(modelPost.getpImage())
                        .placeholder(R.drawable.post_home_deafault_img).into(pImageIv);
            }
        }

    }

    public void setOnPostClickListeners(HomePostListeners homePostListeners) {
        this.homePostListeners = homePostListeners;
    }

    private static DiffUtil.ItemCallback<ModelPost> diffCallback = new DiffUtil.ItemCallback<ModelPost>() {
        @Override
        public boolean areItemsTheSame(@NonNull ModelPost oldItem, @NonNull ModelPost newItem) {
            return oldItem.getpId().equals(newItem.getpId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ModelPost oldItem, @NonNull ModelPost newItem) {
            return !oldItem.equals(newItem);
        }
    };

    public interface HomePostListeners {
        void onUserClicked(ModelPost modelPost);

        void onRateClicked(int position, ModelPost modelPost);

        void onImageClicked(ModelPost modelPost);

        void onCommentClicked(ModelPost modelPost);

        void onShareClicked(ModelPost modelPost);

        void onContentClicked(ModelPost modelPost);

        void onDeleteClicked(ModelPost modelPost);
    }

}
