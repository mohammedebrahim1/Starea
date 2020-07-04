package com.example.geek.starea.Models;

public class ModelCourse {
    String courseName ;
    String instructorName ;
    String instructorPhoto ;

    public ModelCourse() {
    }

    public ModelCourse(String courseName, String instructorName, String instructorPhoto) {
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.instructorPhoto = instructorPhoto;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorPhoto() {
        return instructorPhoto;
    }

    public void setInstructorPhoto(String instructorPhoto) {
        this.instructorPhoto = instructorPhoto;
    }
}
