package com.example.geek.starea;

import android.os.Bundle;

import com.example.geek.starea.Adapters.TimeLineAdapter;
import com.example.geek.starea.Models.TimelineItem;
import com.example.geek.starea.utils.DataSource;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView timelineRv;
    private TimeLineAdapter adapter;
    private List<TimelineItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniRV();
        getListData();
        setupAdapter();


    }

    private void setupAdapter() {

        adapter = new TimeLineAdapter(this,mData);
        timelineRv.setAdapter(adapter);
    }

    private void getListData() {
        mData = DataSource.getTimelineData();
    }

    private void iniRV() {

        timelineRv = findViewById(R.id.timeline_rv);
        timelineRv.setLayoutManager(new LinearLayoutManager(this));

    }
}
