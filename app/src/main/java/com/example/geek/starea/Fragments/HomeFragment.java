package com.example.geek.starea.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geek.starea.Adapters.TimeLineAdapter;
import com.example.geek.starea.Models.TimelineItem;
import com.example.geek.starea.R;
import com.example.geek.starea.utils.DataSource;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        timelineRv = view.findViewById(R.id.timeline_rv);
        timelineRv.setLayoutManager(new LinearLayoutManager(this.getActivity()));

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

}
