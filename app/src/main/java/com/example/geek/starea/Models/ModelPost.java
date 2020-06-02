package com.example.geek.starea.Models;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.List;

public class ModelPost {

    String pId, pContent, pRates, pComments, pImage, pTime, uid, uName, uEmail, uDp;
    @Exclude
    boolean ratedPost;

    public ModelPost() {
    }

    public ModelPost(String pId, String pContent, String pRates, String pComments, String pImage, String pTime, String uid, String uName, String uEmail, String uDp) {
        this.pId = pId;
        this.pContent = pContent;
        this.pRates = pRates;
        this.pComments = pComments;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uName = uName;
        this.uEmail = uEmail;
        this.uDp = uDp;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpContent() {
        return pContent;
    }

    public void setpContent(String pContent) {
        this.pContent = pContent;
    }

    public String getpRates() {
        return pRates;
    }

    public void setpRates(String pRates) {
        this.pRates = pRates;
    }

    public String getpComments() {
        return pComments;
    }

    public void setpComments(String pComments) {
        this.pComments = pComments;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    @Exclude
    public boolean isRated() {
        return ratedPost;
    }

    @Exclude
    public void setIsRated(Boolean isRated) {
        this.ratedPost = isRated;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof ModelPost))
            return super.equals(obj);

        ModelPost other = (ModelPost) obj;

        return this.getpId() == other.getpId() && this.getpImage().equals(other.getpImage())
                && this.getuName().equals(other.getuName()) && this.getpRates().equals(other.getpRates());

    }
}