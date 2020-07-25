package com.example.geek.starea.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.geek.starea.Adapters.AdapterPost;
import com.example.geek.starea.AddPostActivity;
import com.example.geek.starea.Auth.LoginActivity;
import com.example.geek.starea.Models.ModelPost;
import com.example.geek.starea.PostDetailActivity;
import com.example.geek.starea.QrActivity;
import com.example.geek.starea.R;
import com.example.geek.starea.ThereProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.util.ArrayList;


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
    FloatingActionButton qrBtn;

    boolean mProcessRate;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        qrBtn = view.findViewById(R.id.fab_home_post);
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
        final SwipeRefreshLayout homeSwipe = view.findViewById(R.id.homeSwipe);
        homeSwipe.setColorSchemeResources(R.color.colorAccent);
        homeSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO Logic Here,, el data btsync lw7dha mn3'er refresh ,, msh m7tag el swipe
                // bs hasebholk 3shan lw a7tagto ba3dyn
                homeSwipe.setRefreshing(false);
            }
        });
        qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , QrActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void loadPosts() {
        loadRates();
        // path of all posts
        DatabaseReference dbPostsRef = FirebaseDatabase.getInstance().getReference("Posts");
        dbPostsRef.keepSynced(true);
        // get all data from path
        dbPostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    if (modelPost.getUid().equals(user.getUid()))
                        modelPost.setIsOwner(true);
                    for (String s : ratedPosts)
                        if (s.equals(modelPost.getpId()))
                            modelPost.setIsRated(true);
                    postList.add(modelPost);
                }

//                for (int i = 0; i < postList.size(); i++) {
//                    for (String s : ratedPosts) {
//                        if (s.equals(postList.get(i).getpId()))
//                            postList.get(i).setIsRated(true);
//                        else postList.get(i).setIsRated(false);
//                    }
//                }
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
        DatabaseReference ratesRef = FirebaseDatabase.getInstance().getReference("Rates");
        // get all data from path
        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds1 : dataSnapshot.getChildren())
                    for (DataSnapshot ds2 : ds1.getChildren())
                        if (ds2.getKey().equals(user.getUid()))
                            ratedPosts.add(ds1.getKey());

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

    private void deleteWithImage(final String pId, String pImage) {
        // progress bar
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Deleting...");
        // 1- delete image by url
        // 2- delete post from db by id
        StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // image deleted , now delete from db
                Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue(); // remove value of pid from firebase

                        }
                        Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), " Error occurred", Toast.LENGTH_LONG).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // error occurred
                pd.dismiss();
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void deleteWithoutImage(String pId) {
        // progress bar
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Deleting...");
        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().removeValue(); // remove value of pid from firebase

                }
                Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), " Error occurred", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onUserClicked(ModelPost modelPost) {
        Intent intent = new Intent(getActivity(), ThereProfileActivity.class);
        intent.putExtra("uid", modelPost.getUid());
        startActivity(intent);
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
                    if (dataSnapshot.child(modelPost.getpId()).hasChild(user.getUid())) {
                        // Rated before  so delete rate
                        postsRef.child(modelPost.getpId()).child("pRates").setValue("" + (pRates - 1));
                        ratesRef.child(modelPost.getpId()).child(user.getUid()).removeValue();
                        modelPost.setIsRated(false);
                        modelPost.setpRates(String.valueOf(pRates - 1));
                    } else {
                        // not rated so rate post
                        postsRef.child(modelPost.getpId()).child("pRates").setValue("" + (pRates + 1));
                        ratesRef.child(modelPost.getpId()).child(user.getUid()).setValue("Rated");
                        modelPost.setIsRated(true);
                        modelPost.setpRates(String.valueOf(pRates + 1));
                    }
                    mProcessRate = false;
                    postList.set(position, modelPost);
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onImageClicked(ModelPost modelPost) {

    }

    @Override
    public void onCommentClicked(ModelPost modelPost) {
        // start post detail activity
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra("postId", modelPost.getpId());
        startActivity(intent);
    }

    @Override
    public void onShareClicked(ModelPost modelPost) {
        Toast.makeText(getActivity(), "Share... ", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onContentClicked(ModelPost modelPost) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra("postId", modelPost.getpId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(ModelPost modelPost) {
        if (modelPost.getpImage().equals("no Image")) {
            deleteWithoutImage(modelPost.getpId());
        } else {
            deleteWithImage(modelPost.getpId(), modelPost.getpImage());
        }
    }
}


