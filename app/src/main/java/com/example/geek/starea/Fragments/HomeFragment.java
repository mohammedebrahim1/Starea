package com.example.geek.starea.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.geek.starea.Adapters.AdapterPost;
import com.example.geek.starea.AddPostActivity;
import com.example.geek.starea.Auth.LoginActivity;
import com.example.geek.starea.Models.ModelPost;
import com.example.geek.starea.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements AdapterPost.HomePostListeners {


    public HomeFragment() {
        // Required empty public constructor
    }

    RecyclerView timelineRv;
    AdapterPost adapter = new AdapterPost();
    ArrayList<ModelPost> postList;
    ArrayList<String> ratedPosts;

    boolean mProcessRate;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        // recycler view
        timelineRv = view.findViewById(R.id.recycler_view_post);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // to view new posts
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recycler
        timelineRv.setLayoutManager(layoutManager);
        // init postlist
        postList = new ArrayList<>();
        timelineRv.setAdapter(adapter);
        adapter.setOnPostClickListeners(this);
        loadPosts();
        ((SimpleItemAnimator) timelineRv.getItemAnimator()).setSupportsChangeAnimations(false);
        return view;
    }

    private void loadPosts() {
        // path of all posts
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        // get all data from path
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                }
                loadRates();
                for (int i = 0; i < postList.size(); i++) {
                    for (String s : ratedPosts) {
                        if (s.equals(postList.get(i).getpId()))
                            postList.get(i).setIsRated(true);
                        else postList.get(i).setIsRated(false);
                    }
                }
                adapter.submitList(postList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // in case of error loading data
                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void loadRates() {
        ratedPosts = new ArrayList<>();
        // path of all posts
        DatabaseReference refref = FirebaseDatabase.getInstance().getReference("Rates");
        // get all data from path
        refref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    Log.d("NENENE", ds.getKey());
                    Log.d("NENENE", ds.getValue().toString());
                    Log.d("NENENE", ds.getValue().toString());
                    if (dataSnapshot.hasChild(user.getUid())) {

                        ratedPosts.add(ds.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchPosts(final String searchQuery) {
        // path of all posts
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        // get all data from path
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    if (modelPost.getpContent().toLowerCase().contains(searchQuery.toLowerCase())) {
                        postList.add(modelPost);
                    }
                }
                adapter.submitList(postList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // in case of error loading data
                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // to show menu in fragment
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar, menu);
        //Search View
        MenuItem item = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        //Search Listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // called when user click search button from keyboard
                // if search not empty ok
                if (!TextUtils.isEmpty(query.trim())) {
                    searchPosts(query);

                }
                // search is empty list all users
                else {
                    loadPosts();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // called when user press any letter on keyboard
                // if search not empty ok
                if (!TextUtils.isEmpty(newText.trim())) {
                    searchPosts(newText);

                }
                // search is empty list all users
                else {
                    loadPosts();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_post) {
            startActivity(new Intent(getActivity(), AddPostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void chickUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onRateClicked(final int position, final ModelPost modelPost) {
        //                Toast.makeText(context, " ...Rate " , Toast.LENGTH_LONG).show();
        final DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
        final DatabaseReference ratesRef = FirebaseDatabase.getInstance().getReference("Rates");
        final int pRates = Integer.parseInt(modelPost.getpRates());
        mProcessRate = true;
        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mProcessRate) {
                    if (dataSnapshot.child(modelPost.getpId()).hasChild(modelPost.getUid())) {
                        // Rated before  so delete rate
                        postsRef.child(modelPost.getpId()).child("pRates").setValue("" + (pRates - 1));
                        ratesRef.child(modelPost.getpId()).child(modelPost.getUid()).removeValue();
                        modelPost.setIsRated(false);
                        modelPost.setpRates(String.valueOf(pRates - 1));
                        mProcessRate = false;
                    } else {
                        // not rated so rate post
                        postsRef.child(modelPost.getpId()).child("pRates").setValue("" + (pRates + 1));
                        ratesRef.child(modelPost.getpId()).child(modelPost.getUid()).setValue("Rated");
                        modelPost.setIsRated(true);
                        modelPost.setpRates(String.valueOf(pRates + 1));
                        mProcessRate = false;
                    }
                    postList.set(position, modelPost);
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("POPOO", " " + position);
    }

    @Override
    public void onCommentClicked(ModelPost modelPost) {

    }

    @Override
    public void onShareClicked(ModelPost modelPost) {

    }

    @Override
    public void onMoreClicked(ModelPost modelPost) {

    }

    @Override
    public void onContentClicked(ModelPost modelPost) {

    }
}


