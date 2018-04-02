package com.example.spatel.giphyexample.utils;

import android.content.Context;
import android.widget.ImageView;

import com.example.spatel.giphyexample.GlideApp;
import com.example.spatel.giphyexample.R;

/**
 * Created by spatel on 01-04-2018.
 */
public class ImageUtils {

    public static void loadImage(Context context, String imageUrl, ImageView imageView){
        GlideApp.with(context)
                .load(imageUrl)
                .dontAnimate()
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .into(imageView);
    }
}
