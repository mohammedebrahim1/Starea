package com.example.geek.starea.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geek.starea.Models.TimelineItem;
import com.example.geek.starea.R;
import com.example.geek.starea.utils.Constant;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeLineAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private List<TimelineItem> mData;

    public TimeLineAdapter(Context mContext, List<TimelineItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType){

            case (Constant.ITEM_HEADER_TEXT_VIEWTYPE):
                view = LayoutInflater.from(mContext).inflate(R.layout.item_header,parent,false);
                return new HeaderTextViewHolder(view);

            case (Constant.ITEM_POST_TEXT_VIEWTYPE):
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_text,parent,false);
                return new PostTextViewHolder(view);

            case (Constant.ITEM_POST_VIDEO_VIEWTYPE):
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_video,parent,false);
                return new PostVideoViewHolder(view);

            default:throw new IllegalArgumentException();

        }

    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        holder.setData(mData.get(position));

    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        if (mData!=null){
            return mData.size();
        }
        else{
            return 0;
        }
    }
}
