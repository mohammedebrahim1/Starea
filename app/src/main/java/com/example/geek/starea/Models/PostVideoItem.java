package com.example.geek.starea.Models;

public class PostVideoItem {

    private String videoURL;
    private int imgUser;
    private String time;

    public PostVideoItem(String videoURL, int imgUser, String time) {
        this.videoURL = videoURL;
        this.imgUser = imgUser;
        this.time = time;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public int getImgUser() {
        return imgUser;
    }

    public void setImgUser(int imgUser) {
        this.imgUser = imgUser;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
