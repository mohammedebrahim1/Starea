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

import com.example.geek.starea.Adapters.AdapterUser;
import com.example.geek.starea.AddPostActivity;
import com.example.geek.starea.Auth.LoginActivity;
import com.example.geek.starea.Models.ModelUser;
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
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassRoomsFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterUser adapterUser;
    List<ModelUser> userList;
    FirebaseAuth firebaseAuth;

    public ClassRoomsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_class_rooms, container, false);
        // init Recycler
        recyclerView = view.findViewById(R.id.users_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        // init Users List
        userList = new ArrayList<>();
        //get all users in database
        getAllUsers();
        return view;
    }
    private void getAllUsers() {
        //get current user
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        // get path from database "Users" containing info
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        //get all data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    // get all users expect current user
                   // Toast.makeText(getActivity() ,"mo" +  modelUser.getUid() , Toast.LENGTH_LONG).show();
                  //  Toast.makeText(getActivity() ,"fu" +  fUser.getUid() , Toast.LENGTH_LONG).show();
                    if (!fUser.getUid().equals(modelUser.getUid())){
                        userList.add(modelUser);
                    }
                    //Adapter
                    adapterUser = new AdapterUser(getActivity() , userList);
                    // set adapter to recycler
                    recyclerView.setAdapter(adapterUser);


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    searchUser(query);

                }
                // search is empty list all users
                else {
                    getAllUsers();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // called when user press any letter on keyboard
                // if search not empty ok
                if (!TextUtils.isEmpty(newText.trim())){
                    searchUser(newText);

                }
                // search is empty list all users
                else {
                    getAllUsers();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu , inflater);
    }

    private void searchUser(final String query) {

        //get current user
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        // get path from database "Users" containing info
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        //get all data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    // get all users expect current user

                    if (  !Objects.requireNonNull(fUser).getUid().equals(Objects.requireNonNull(modelUser).getUid())){
                        if (modelUser.getName().toLowerCase().contains(query.toLowerCase())
                        || modelUser.getEmail().toLowerCase().contains(query.toLowerCase())){
                            userList.add(modelUser);

                        }

                    }
                    //Adapter
                    adapterUser = new AdapterUser(getActivity() , userList);
                    //refresh Adapter
                    adapterUser.notifyDataSetChanged();
                    // set adapter to recycler
                    recyclerView.setAdapter(adapterUser);


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
