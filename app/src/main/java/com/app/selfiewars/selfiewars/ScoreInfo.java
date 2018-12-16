package com.app.selfiewars.selfiewars;

public class ScoreInfo {
   private String uid;
    private Integer scoreValue;

    public ScoreInfo(){

    }
    public ScoreInfo(String uid, Integer scoreValue) {
        this.uid = uid;
        this.scoreValue = scoreValue;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(Integer scoreValue) {
        this.scoreValue = scoreValue;
    }
}
