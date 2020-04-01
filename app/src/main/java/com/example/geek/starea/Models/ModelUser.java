package com.example.geek.starea.Models;

public class ModelUser {
    String name , email , search , phone , photo , cover  , uId;

    public ModelUser() {
    }

    public ModelUser(String name, String email, String search, String phone, String photo, String cover, String uId) {
        this.name = name;
        this.email = email;
        this.search = search;
        this.phone = phone;
        this.photo = photo;
        this.cover = cover;
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUid() {
        return uId;
    }

    public void setUid(String uId) {
        this.uId = uId;
    }
}

