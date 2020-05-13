package com.example.geek.starea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geek.starea.Adapters.AdapterPost;
import com.example.geek.starea.Auth.LoginActivity;
import com.example.geek.starea.Models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ThereProfileActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth ;
    RecyclerView profileRecycler;
    TextView mNameTv , mEmailTv;
    ImageView mProfileIV , mCoverIv;
    List<ModelPost> postList;
    AdapterPost adapterPost;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_there_profile);
        // init views
        profileRecycler = findViewById(R.id.profile_recycler);
        mNameTv = findViewById(R.id.nameTv);
        mEmailTv = findViewById(R.id.emailTv);
        mProfileIV = findViewById(R.id.profileIv);
        mCoverIv = findViewById(R.id.coverIv);
        // instance
        firebaseAuth = FirebaseAuth.getInstance();
        // get clicked user id for retrieve data
        Intent intent = getIntent();
        uid =  intent.getStringExtra("uid");
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uId").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check database untill get data
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //get data
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String avatar = "" + ds.child("photo").getValue();
                    String cover = "" + ds.child("cover").getValue();
                    //set data
                    mNameTv.setText(name);
                    mEmailTv.setText(email);
                    try {
                        Picasso.get().load(cover).into(mCoverIv);
                    }
                    catch (Exception e) {
                        // Picasso.get().load(R.drawable.ic_add_image).into(mCoverIv);
                    }

                    try {
                        // in succses case
                        Picasso.get().load(avatar).into(mProfileIV);

                    }
                    catch (Exception e){
                        // if failed load default
                        Picasso.get().load(R.drawable.ic_add_image).into(mProfileIV); }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        postList = new ArrayList<>();

        chickUserStatus();
        loadHisPosts();

    }

    private void loadHisPosts() {
        // Linear layout for recycler
        LinearLayoutManager layoutManager = new LinearLayoutManager(ThereProfileActivity.this);
        // load from last to show newst posts first
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        // set layout to recycler
        profileRecycler.setLayoutManager(layoutManager);
        // init posts list
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        //Query to load posts
        Query query = reference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    postList.add(modelPost);
                    adapterPost = new AdapterPost(ThereProfileActivity.this , postList);
                    // set adapter
                    profileRecycler.setAdapter(adapterPost);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ThereProfileActivity.this , "" + databaseError.getMessage() , Toast.LENGTH_LONG).show();

            }
        });


    }
    private void searchHisPosts(final String searchQuery) {
        // Linear layout for recycler
        LinearLayoutManager layoutManager = new LinearLayoutManager(ThereProfileActivity.this);
        // load from last to show newst posts first
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        // set layout to recycler
        profileRecycler.setLayoutManager(layoutManager);
        // init posts list
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        //Query to load posts
        Query query = reference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    if (modelPost.getpContent().toLowerCase().contains(searchQuery.toLowerCase())){

                        postList.add(modelPost);
                    }
                    adapterPost = new AdapterPost(ThereProfileActivity.this , postList);
                    // set adapter
                    profileRecycler.setAdapter(adapterPost);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ThereProfileActivity.this , "" + databaseError.getMessage() , Toast.LENGTH_LONG).show();

            }
        });



    }

    private void chickUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){


        }
        else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar , menu);
        menu.findItem(R.id.post_action).setVisible(false);
        //Search View
        MenuItem item = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        //Search Listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // called when user click search button from keyboard
                // if search not empty ok
                if (!TextUtils.isEmpty(query.trim())){
                    searchHisPosts(query);

                }
                // search is empty list all users
                else {
                    loadHisPosts();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // called when user press any letter on keyboard
                // if search not empty ok
                if (!TextUtils.isEmpty(newText.trim())){
                    searchHisPosts(newText);

                }
                // search is empty list all users
                else {
                    loadHisPosts();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}