package com.example.geek.starea.Adapters;

import android.view.View;

import com.example.geek.starea.Models.TimelineItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    abstract void setData (TimelineItem item);
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
