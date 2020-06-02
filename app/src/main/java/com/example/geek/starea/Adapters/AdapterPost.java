package com.example.geek.starea.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geek.starea.Models.ModelPost;
import com.example.geek.starea.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPost extends ListAdapter<ModelPost, AdapterPost.PostHolder> {
    Context context;
    List<ModelPost> postList;
    String myUid;
    private DatabaseReference ratesRef;
    private DatabaseReference postsRef;
    boolean mProcessRate = false;

    private HomePostListeners homePostListeners;

    public AdapterPost() {
        super(diffCallback);
//        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
//        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
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
        // get data

//        // handle buttons clicks
//        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showMoreOptions(holder.moreBtn, uid, myUid, pId, pImage);
//            }
//        });
//        holder.rateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(context, " ...Rate " , Toast.LENGTH_LONG).show();
//                final int pRates = Integer.parseInt(postList.get(position).getpRates());
//                mProcessRate = true;
//                final String postId = postList.get(position).getpId();
//                ratesRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (mProcessRate) {
//                            if (dataSnapshot.child(postId).hasChild(myUid)) {
//                                // Rated before  so delete rate
//                                postsRef.child(postId).child("pRates").setValue("" + (pRates - 1));
//                                ratesRef.child(postId).child(myUid).removeValue();
//                                mProcessRate = false;
//                            } else {
//                                // not rated so rate post
//                                postsRef.child(postId).child("pRates").setValue("" + (pRates + 1));
//                                ratesRef.child(postId).child(myUid).setValue("Rated");
//                                mProcessRate = false;
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });
//        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(context, "Comment... " , Toast.LENGTH_LONG).show();
//                // start post detail activity
//                Intent intent = new Intent(context, PostDetailActivity.class);
//                intent.putExtra("postId", pId);
//                context.startActivity(intent);
//            }
//        });
//        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Share... ", Toast.LENGTH_LONG).show();
//            }
//        });
//        holder.uNameTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // on click go to clicked user profile and show his data
//                Intent intent = new Intent(context, ThereProfileActivity.class);
//                intent.putExtra("uid", uid);
//                context.startActivity(intent);
//            }
//        });
//        holder.pContentTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // start post detail activity
//                Intent intent = new Intent(context, PostDetailActivity.class);
//                intent.putExtra("postId", pId);
//                context.startActivity(intent);
//            }
//        });
        holder.bind(getItem(position));
    }

//    private void showMoreOptions(ImageButton moreBtn, String uid, String myUid, final String pId, final String pImage) {
//        // creating popup menu have options
//        PopupMenu popupMenu = new PopupMenu(context, moreBtn, Gravity.END);
//        // show delete post to posts of current user only
//        if (uid.equals(myUid)) {
//            // add items on menu
//            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
//
//        }
//
//        // item click handle
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int id = item.getItemId();
//                if (id == 0) {
//                    // delete is clicked
//                    beginDelete(pId, pImage);
//
//                }
//
//                return false;
//            }
//        });
//        // show menu
//        popupMenu.show();
//    }
//
//    private void beginDelete(String pId, String pImage) {
//        // post can be with or without image
//        if (pImage.equals("no Image")) {
//            // without image
//            deleteWithoutImage(pId);
//
//        } else {
//            // with image
//            deleteWithImage(pId, pImage);
//
//
//        }
//    }
//
//    private void deleteWithImage(final String pId, String pImage) {
//        // progress bar
//        final ProgressDialog pd = new ProgressDialog(context);
//        pd.setMessage("Deleting...");
//        // 1- delete image by url
//        // 2- delete post from db by id
//        StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
//        picref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                // image deleted , now delete from db
//                Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                            ds.getRef().removeValue(); // remove value of pid from firebase
//
//                        }
//                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
//                        pd.dismiss();
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(context, " Error occurred", Toast.LENGTH_LONG).show();
//
//                    }
//                });
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                // error occurred
//                pd.dismiss();
//                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        });
//    }
//
//    private void deleteWithoutImage(String pId) {
//        // progress bar
//        final ProgressDialog pd = new ProgressDialog(context);
//        pd.setMessage("Deleting...");
//        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    ds.getRef().removeValue(); // remove value of pid from firebase
//
//                }
//                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
//                pd.dismiss();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(context, " Error occurred", Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//    }


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

            rateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homePostListeners.onRateClicked(getAdapterPosition(), getItem(getAdapterPosition()));
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
            moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homePostListeners.onMoreClicked(getItem(getAdapterPosition()));
                }
            });
            pContentTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homePostListeners.onContentClicked(getItem(getAdapterPosition()));
                }
            });

        }

        void bind(ModelPost modelPost) {
            final String uid = modelPost.getUid();
            String uEmail = modelPost.getuEmail();
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
//            setRates(modelPost.getpRates());
            if (modelPost.isRated()) {
                rateBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_full_star_black, 0, 0, 0);
            } else {
                // user has not rated this post
                rateBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_border_black_24dp, 0, 0, 0);
            }
            // set user dp
            try {
                Picasso.get().load(modelPost.getuDp()).placeholder(R.drawable.ic_default_image).into(uImageIv);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // in case post has no image
            if (modelPost.getpImage().equals("no Image")) {
                pImageIv.setVisibility(View.GONE);

            } else {

                try {
                    Picasso.get().load(modelPost.getpImage()).placeholder(R.drawable.post_home_deafault_img).into(pImageIv);
                    pImageIv.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }


//        private void setRates(final String postKey) {
//            ratesRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.child(postKey).hasChild(myUid)) {
//                        // user has rate this post
//                        // change drawable star to rated
//                        rateBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_full_star_black, 0, 0, 0);
//
//
//                    } else {
//                        // user has not rated this post
//                        rateBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_border_black_24dp, 0, 0, 0);
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
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
        void onRateClicked(int position, ModelPost modelPost);

        void onCommentClicked(ModelPost modelPost);

        void onShareClicked(ModelPost modelPost);

        void onMoreClicked(ModelPost modelPost);

        void onContentClicked(ModelPost modelPost);
    }

}
