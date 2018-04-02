package com.example.spatel.giphyexample.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by spatel on 01-04-2018.
 */
public class Utils {

    public static void hideSoftKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
