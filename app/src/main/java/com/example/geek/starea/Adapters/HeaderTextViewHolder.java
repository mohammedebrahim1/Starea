package com.example.geek.starea.Adapters;

import android.view.View;
import android.widget.TextView;

import com.example.geek.starea.Models.HeaderTextItem;
import com.example.geek.starea.Models.TimelineItem;
import com.example.geek.starea.R;

import androidx.annotation.NonNull;

public class HeaderTextViewHolder extends BaseViewHolder {

    private TextView tvHeader;

    public HeaderTextViewHolder(@NonNull View itemView) {
        super(itemView);
        tvHeader = itemView.findViewById(R.id.header_text);
    }

    @Override
    void setData(TimelineItem item) {
        HeaderTextItem headerTextItem = item.getHeaderTextItem();
        tvHeader.setText(headerTextItem.getHeaderText());

    }
}
