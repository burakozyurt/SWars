package com.app.selfiewars.selfiewars;

public class GuessItUserData {
    private String uid;
    private Integer age;
    private String photoUrl;
    private String userName;

    public GuessItUserData() {
    }

    public GuessItUserData(String uid, Integer age, String photoUrl, String userName) {
        this.uid = uid;
        this.age = age;
        this.photoUrl = photoUrl;
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
