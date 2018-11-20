package com.app.selfiewars.selfiewars;

import android.content.Intent;

public class UserProperties {
    String displayName;
    String userName;
    String photoUrl;
    Integer Age;

    public UserProperties() {
    }

    public UserProperties(String displayName, String userName, String photoUrl, Integer age) {
        this.displayName = displayName;
        this.userName = userName;
        this.photoUrl = photoUrl;
        Age = age;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }
}
