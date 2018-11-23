package com.app.selfiewars.selfiewars;

import android.content.Intent;

public class RankingInfoActivity {
    int starImageView;
    int userPhotoImageView;
    String userName;

    public RankingInfoActivity() {
    }

    public RankingInfoActivity(int starImageView, int userPhotoImageView, String userName) {
        this.starImageView = starImageView;
        this.userPhotoImageView = userPhotoImageView;
        this.userName = userName;
    }

    public int getStarImageView() {
        return starImageView;
    }

    public void setStarImageView(int starImageView) {
        this.starImageView = starImageView;
    }

    public int getUserPhotoImageView() {
        return userPhotoImageView;
    }

    public void setUserPhotoImageView(int userPhotoImageView) {
        this.userPhotoImageView = userPhotoImageView;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
