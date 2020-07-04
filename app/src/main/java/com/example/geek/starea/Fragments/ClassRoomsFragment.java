package com.example.geek.starea.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geek.starea.Adapters.AdapterCourse;
import com.example.geek.starea.Adapters.DataFakeGenerator;
import com.example.geek.starea.AddPostActivity;
import com.example.geek.starea.Auth.LoginActivity;
import com.example.geek.starea.Models.ModelCourse;
import com.example.geek.starea.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassRoomsFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterCourse adapterCourse;
    List<ModelCourse> courseList;
    FirebaseAuth firebaseAuth;

    public ClassRoomsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_class_rooms, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        // init Recycler
        recyclerView = view.findViewById(R.id.courses_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity() , 2));
        // init Users List
        courseList = new ArrayList<>();
        //get all users in database
     //   getAllUsers();
        //Adapter
        adapterCourse = new AdapterCourse(getActivity() , DataFakeGenerator.getData());
        // set adapter to recycler
        recyclerView.setAdapter(adapterCourse);
        return view;
    }

//    private void getAllUsers() {
//        //get current user
//        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        // get path from database "Users" containing info
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//        //get all data
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    ModelUser modelUser = ds.getValue(ModelUser.class);
//                    // get all users expect current user
//                   // Toast.makeText(getActivity() ,"mo" +  modelUser.getUid() , Toast.LENGTH_LONG).show();
//                  //  Toast.makeText(getActivity() ,"fu" +  fUser.getUid() , Toast.LENGTH_LONG).show();
//                    if (!fUser.getUid().equals(modelUser.getUid())){
//                        userList.add(modelUser);
//                    }
//                    //Adapter
//                    adapterUser = new AdapterUser(getActivity() , userList);
//                    // set adapter to recycler
//                    recyclerView.setAdapter(adapterUser);
//
//
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // to show menu in fragment
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar , menu);
        //Search View
        menu.findItem(R.id.search_action).setVisible(false);

        super.onCreateOptionsMenu(menu , inflater);
    }

//    private void searchUser(final String query) {
//
//        //get current user
//        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        // get path from database "Users" containing info
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//        //get all data
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    ModelUser modelUser = ds.getValue(ModelUser.class);
//                    // get all users expect current user
//
//                    if (  !Objects.requireNonNull(fUser).getUid().equals(Objects.requireNonNull(modelUser).getUid())){
//                        if (modelUser.getName().toLowerCase().contains(query.toLowerCase())
//                        || modelUser.getEmail().toLowerCase().contains(query.toLowerCase())){
//                            userList.add(modelUser);
//
//                        }
//
//                    }
//                    //Adapter
//                    adapterUser = new AdapterUser(getActivity() , userList);
//                    //refresh Adapter
//                    adapterUser.notifyDataSetChanged();
//                    // set adapter to recycler
//                    recyclerView.setAdapter(adapterUser);
//
//
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

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
