package com.example.geek.starea.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.geek.starea.Adapters.TimeLineAdapter;
import com.example.geek.starea.AddPostActivity;
import com.example.geek.starea.Auth.LoginActivity;
import com.example.geek.starea.Models.TimelineItem;
import com.example.geek.starea.R;
import com.example.geek.starea.utils.DataSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView timelineRv;
    private TimeLineAdapter adapter;
    private List<TimelineItem> mData;
    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        timelineRv = view.findViewById(R.id.timeline_rv);
        timelineRv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        getListData();
        setupAdapter();
        return view;
    }
    private void setupAdapter() {

        adapter = new TimeLineAdapter(this,mData);
        timelineRv.setAdapter(adapter);
    }

    private void getListData() {
        mData = DataSource.getTimelineData();
    }

    private void iniRV() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // to show menu in fragment
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar , menu);
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


