package com.app.selfiewars.selfiewars;

import android.content.Intent;

public class RankingInfoActivity {
   private int starImageView;
   private String userPhotoImageView;
   private String userName;

    public RankingInfoActivity() {
    }

    public RankingInfoActivity(int starImageView, String userPhotoImageView, String userName) {
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

    public String getUserPhotoImageView() {
        return userPhotoImageView;
    }

    public void setUserPhotoImageView(String userPhotoImageView) {
        this.userPhotoImageView = userPhotoImageView;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
