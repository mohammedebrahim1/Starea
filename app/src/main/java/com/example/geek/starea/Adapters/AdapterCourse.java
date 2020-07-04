package com.example.geek.starea.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geek.starea.Models.ModelCourse;
import com.example.geek.starea.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCourse extends RecyclerView.Adapter<AdapterCourse.myHolder> {
    Context context;

    List<ModelCourse> courseList;

    public AdapterCourse(Context context, List<ModelCourse> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout row user xml

        View view = LayoutInflater.from(context).inflate(R.layout.row_classroom, parent, false);

        return new AdapterCourse.myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        // get data
        String courseName = courseList.get(position).getCourseName();
        String instructorName = courseList.get(position).getInstructorName();
        String instructorPhoto = courseList.get(position).getInstructorPhoto();


        //set data
        holder.mCourseNameTv.setText(courseName);
        holder.mInstructorNameTv.setText(instructorName);
        Picasso.get().load(instructorPhoto).placeholder(R.drawable.avatardraw).fit().into(holder.mAvatarIv);


        // handle item click
        holder.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle btn click

            }
        });

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }


    // view holder class
    class myHolder extends RecyclerView.ViewHolder {
        ImageView mAvatarIv;
        TextView mCourseNameTv, mInstructorNameTv;
        Button joinBtn;

        public myHolder(@NonNull View context) {
            super(context);
            // init views
            mAvatarIv = context.findViewById(R.id.instructorAvatar);
            mCourseNameTv = context.findViewById(R.id.course_nameTv);
            mInstructorNameTv = context.findViewById(R.id.instructor_nameTv);
            joinBtn = context.findViewById(R.id.join_btn);

        }
    }
}
