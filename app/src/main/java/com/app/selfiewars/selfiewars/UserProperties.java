package com.app.selfiewars.selfiewars;

public class UserProperties {
    private String displayName;
    private String email;
    private String userName;
    private String photoUrl;
    private String gender;
    private Integer age;

    public UserProperties() {

    }


    public UserProperties(String displayName, String email, String userName, String photoUrl, String gender, Integer age) {
        this.displayName = displayName;
        this.email = email;
        this.userName = userName;
        this.photoUrl = photoUrl;
        this.gender = gender;
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
