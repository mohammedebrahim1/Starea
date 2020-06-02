package com.example.geek.starea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.geek.starea.Adapters.AdapterComments;
import com.example.geek.starea.Auth.LoginActivity;
import com.example.geek.starea.Models.ModelComment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailActivity extends AppCompatActivity {

    @BindView(R.id.post_text_time)
    TextView pTimeTv;
    @BindView(R.id.post_home_user_img)
    CircularImageView uImageIv;
    @BindView(R.id.post_home_user_name)
    TextView uNameTv;
    @BindView(R.id.post_text_content)
    TextView pContentTv;
    @BindView(R.id.post_more)
    ImageButton moreBtn;
    @BindView(R.id.post_home_image)
    ImageView pImageIv;
    @BindView(R.id.post_rate)
    Button rateBtn;
    @BindView(R.id.post_share)
    Button shareBtn;
    @BindView(R.id.events_layout)
    LinearLayout eventsLayout;
    @BindView(R.id.cAvatarIv)
    CircularImageView cUserIv;
    @BindView(R.id.commentEt)
    EditText commentEt;
    @BindView(R.id.send_btn)
    ImageButton sendBtn;
    @BindView(R.id.post_toolbar)
    Toolbar toolbar;
    @BindView(R.id.add_comment_layout)
    LinearLayout addCommentLayout;
    @BindView(R.id.post_comment)
    Button commentBtn;
    @BindView(R.id.commentsRecycler)
    RecyclerView commentsRecycler;
    // get data about user and post
    String hisUid, myUid, myEmail, myName, myDp,
            postId, pImage, pRates, hisDp, hisName;
    ProgressDialog pd;
    boolean mProcessComment = false;
    boolean mProcessRate = false;
    List<ModelComment> commentList;
    AdapterComments adapterComments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // get id of post from intent
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        chickUserStatus();
        // load user info
        loadUserInfo();
        // load post
        loadPostInfo();

        setRates();
        loadComments();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();

            }
        });
        // handle rate button click
        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratePost();
            }
        });
        // handle more btn click
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOptions();

            }
        });
    }

    private void loadComments() {
        // layout for recycler
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        // set layout to recycler
        commentsRecycler.setLayoutManager(layoutManager);
        commentList = new ArrayList<>();
        // path of comments in db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ModelComment modelComment = ds.getValue(ModelComment.class);
                    commentList.add(modelComment);
                    // set adapter
                    adapterComments = new AdapterComments(getApplicationContext() , commentList);
                    commentsRecycler.setAdapter(adapterComments);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setRates() {
        final DatabaseReference ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).hasChild(myUid)) {
                    // user has rate this post
                    // change drawable star to rated
                    rateBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_full_star_black, 0, 0, 0);


                } else {
                    // user has not rated this post
                    rateBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_border_black_24dp, 0, 0, 0);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void ratePost() {
        mProcessRate = true;
        final DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        final DatabaseReference ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mProcessRate) {
                    if (dataSnapshot.child(postId).hasChild(myUid)) {
                        // Rated before  so delete rate
                        postsRef.child(postId).child("pRates").setValue("" + (Integer.parseInt(pRates) - 1));
                        ratesRef.child(postId).child(myUid).removeValue();
                        mProcessRate = false;
                    } else {
                        // not rated so rate post
                        postsRef.child(postId).child("pRates").setValue("" + (Integer.parseInt(pRates) + 1));
                        ratesRef.child(postId).child(myUid).setValue("Rated");
                        mProcessRate = false;

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void postComment() {
        pd = new ProgressDialog(this);
        pd.setMessage("Adding Comment");
        // get data from edit text

        final String comment = commentEt.getText().toString().trim();
        // Validate data
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "Comment is Empty", Toast.LENGTH_LONG).show();
            return;
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());
        // each post will have a child "comments" that contain comments of the post
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId)
                .child("Comments");
        HashMap<String, Object> hashMap = new HashMap<>();
        // put info
        hashMap.put("cId", timeStamp);
        hashMap.put("comment", comment);
        hashMap.put("timeStamp", timeStamp);
        hashMap.put("uid", myUid);
        hashMap.put("uEmail", myEmail);
        hashMap.put("uDp", myDp);
        hashMap.put("uName", myName);
        // put this data in dp
        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // comment added
                pd.dismiss();
                Toast.makeText(PostDetailActivity.this, "Comment Added...", Toast.LENGTH_LONG).show();
                commentEt.setText("");
                updateCommentCount();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(PostDetailActivity.this, "Error Occurred..." + e.getMessage(), Toast.LENGTH_LONG).show();


            }
        });


    }


    private void updateCommentCount() {
        mProcessComment = true;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mProcessComment) {
                    String comments = "" + dataSnapshot.child("pComments").getValue();
                    int newCommentVal = Integer.parseInt(comments) + 1;
                    reference.child("pComments").setValue("" + newCommentVal);
                    mProcessComment = false;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadUserInfo() {
        // get user info
        Query myref = FirebaseDatabase.getInstance().getReference("Users");
        myref.orderByChild("uId").equalTo(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    myName = "" + ds.child("name").getValue();
                    myDp = "" + ds.child("photo").getValue();
                    // set data
                    try {
                        Picasso.get().load(myDp).placeholder(R.drawable.ic_default_image).into(cUserIv);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_default_image).into(cUserIv);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPostInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = ref.orderByChild("pId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String pContent = "" + ds.child("pContent").getValue();
                    pRates = "" + ds.child("pRates").getValue();
                    String pTimeStamp = "" + ds.child("pTime").getValue();
                    pImage = "" + ds.child("pImage").getValue();
                    hisDp = "" + ds.child("uDp").getValue();
                    hisUid = "" + ds.child("uid").getValue();
                    String uEmail = "" + ds.child("uEmail").getValue();
                    hisName = "" + ds.child("uName").getValue();
                    String commentCount = "" + ds.child("pComments").getValue();
                    // convert timestamp
                    final Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                    calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
                    String pTime = DateFormat.format("hh:mm aa", calendar).toString();
                    // set Data
                    pContentTv.setText(pContent);
                    rateBtn.setText(pRates);
                    pTimeTv.setText(pTime);
                    uNameTv.setText(hisName);
                    commentBtn.setText(commentCount);

                    // in case post has no image
                    if (pImage.equals("no Image")) {
                        pImageIv.setVisibility(View.GONE);

                    } else {

                        try {
                            Picasso.get().load(pImage).placeholder(R.drawable.post_home_deafault_img).into(pImageIv);
                            pImageIv.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    // set user image in post
                    try {
                        Picasso.get().load(hisDp).placeholder(R.drawable.ic_default_image).into(uImageIv);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_default_image).into(uImageIv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMoreOptions() {
        // creating popup menu have options
        PopupMenu popupMenu = new PopupMenu(this, moreBtn, Gravity.END);
        // show delete post to posts of current user only
        if (hisUid.equals(myUid)) {
            // add items on menu
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");

        }

        // item click handle
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 0) {
                    // delete is clicked
                    beginDelete();

                }

                return false;
            }
        });
        // show menu
        popupMenu.show();
    }

    private void beginDelete() {
        // post can be with or without image
        if (pImage.equals("no Image")) {
            // without image
            deleteWithoutImage();

        } else {
            // with image
            deleteWithImage();


        }
    }

    private void deleteWithImage() {
        // progress bar
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Deleting...");
        // 1- delete image by url
        // 2- delete post from db by id
        StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // image deleted , now delete from db
                Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(postId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue(); // remove value of pid from firebase

                        }
                        Toast.makeText(PostDetailActivity.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(PostDetailActivity.this, " Error occurred", Toast.LENGTH_LONG).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // error occurred
                pd.dismiss();
                Toast.makeText(PostDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void deleteWithoutImage() {
        // progress bar
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Deleting...");
        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(postId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().removeValue(); // remove value of pid from firebase

                }
                Toast.makeText(PostDetailActivity.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PostDetailActivity.this, " Error occurred", Toast.LENGTH_LONG).show();

            }
        });

    }


    private void chickUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            myEmail = user.getEmail();
            myUid = user.getUid();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        menu.findItem(R.id.add_post).setVisible(false);
        menu.findItem(R.id.search_action).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            chickUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
