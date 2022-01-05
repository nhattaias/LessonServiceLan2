package com.example.lessonservice;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

public class App extends Application {
    private static App instance;
    private final MutableLiveData<String>pathDownload = new MutableLiveData<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public MutableLiveData<String> getPathDownload() {
        return pathDownload;
    }

    public static App getInstance() {
        return instance;
    }
}
