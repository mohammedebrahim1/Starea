package com.example.geek.starea.Adapters;

import com.example.geek.starea.Models.ModelCourse;

import java.util.ArrayList;
import java.util.List;

public class DataFakeGenerator {
    public static List<ModelCourse> getData(){
        List<ModelCourse> courseList = new ArrayList<>();
        for (int i = 0 ; i<10 ; i++) {
            ModelCourse course = new ModelCourse("Data Structures", "Dr / Tawfik Atteya", "https://i.ibb.co/C0kpvrg/unnamed.png");
            courseList.add(course);
        }
        return courseList;

    }
}
