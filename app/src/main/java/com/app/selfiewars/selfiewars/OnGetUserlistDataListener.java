package com.app.selfiewars.selfiewars;

import java.util.List;

public interface OnGetUserlistDataListener {
    public void onStart();
    public void onProgress(String string);
    public void onSuccess(List<GuessItUserData> guessItUserDataList, Boolean isBeta);
    public void onFailed();
}
