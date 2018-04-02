package com.example.spatel.giphyexample;

import android.app.Application;

import com.example.spatel.giphyexample.searchvideo.model.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * Created by spatel on 01-04-2018.
 */
public class GiphyApp extends Application {

    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();

        boxStore = MyObjectBox.builder().androidContext(GiphyApp.this).build();
//        if (BuildConfig.DEBUG) {
//            new AndroidObjectBrowser(boxStore).start(this);
//        }
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

}
