package com.example.geek.starea.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.geek.starea.Adapters.AdapterPost;
import com.example.geek.starea.Adapters.TimeLineAdapter;
import com.example.geek.starea.AddPostActivity;
import com.example.geek.starea.Auth.LoginActivity;
import com.example.geek.starea.Models.ModelPost;
import com.example.geek.starea.Models.TimelineItem;
import com.example.geek.starea.R;
import com.example.geek.starea.utils.DataSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

     RecyclerView timelineRv;
    AdapterPost adapter;
    List<ModelPost> postList;


    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
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
        loadPosts();

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
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                    adapter = new AdapterPost(getActivity() , postList);
                    timelineRv.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // in case of error loading data
                Toast.makeText(getActivity() ,"" + databaseError.getMessage() , Toast.LENGTH_LONG).show();

            }
        });
    }
    private  void searchPosts(final String searchQuery){
        // path of all posts
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        // get all data from path
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    if (modelPost.getpContent().toLowerCase().contains(searchQuery.toLowerCase())){
                        postList.add(modelPost);
                    }

                    adapter = new AdapterPost(getActivity() , postList);
                    timelineRv.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // in case of error loading data
                Toast.makeText(getActivity() ,"" + databaseError.getMessage() , Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // to show menu in fragment
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar , menu);
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
                if (!TextUtils.isEmpty(newText.trim())){
                    searchPosts(newText);

                }
                // search is empty list all users
                else {
                    loadPosts();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu , inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();
        if (id == R.id.add_post){
            startActivity(new Intent(getActivity(), AddPostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void chickUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){

        }
        else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

}


