package com.example.geek.starea.utils;

import com.example.geek.starea.Models.HeaderTextItem;
import com.example.geek.starea.Models.PostTextItem;
import com.example.geek.starea.Models.PostVideoItem;
import com.example.geek.starea.Models.TimelineItem;
import com.example.geek.starea.R;

import java.util.ArrayList;
import java.util.List;



public class DataSource {
    public static List<TimelineItem> getTimelineData (){
        List<TimelineItem> mdata = new ArrayList<>();

        //create header item
        HeaderTextItem itemHeader = new HeaderTextItem("3 days ago");
        TimelineItem headerTimelineItem = new TimelineItem(itemHeader);

        //create Post Text Item
        PostTextItem postTextItem = new PostTextItem("Yarab n3ml mshroo3 nag7 w nt5arag yarab", R.drawable.fcb,"20:35");
        TimelineItem postTextTimelineItem = new TimelineItem(postTextItem);

        //create Post Video Item
        PostVideoItem postVideoItem = new PostVideoItem("", R.drawable.fcb,"22:15");
        TimelineItem postVideoTimelineItem = new TimelineItem(postVideoItem);

        mdata.add(headerTimelineItem);
        mdata.add(postTextTimelineItem);
        mdata.add(postVideoTimelineItem);

        return mdata;
    }
}
