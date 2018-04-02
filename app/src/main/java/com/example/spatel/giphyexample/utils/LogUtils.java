package com.example.spatel.giphyexample.utils;

import android.util.Log;

/**
 * Created by spatel on 01-04-2018.
 */
public class LogUtils {

    public static void I(String tag, String msg){
        Log.i(tag, msg);
    }

    public static void V(String tag, String msg){
        Log.v(tag, msg);
    }

    public static void E(String tag, String msg){
        Log.e(tag, msg);
    }
}
