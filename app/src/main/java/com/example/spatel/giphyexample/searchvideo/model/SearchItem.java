package com.example.spatel.giphyexample.searchvideo.model;

/**
 * Created by spatel on 01-04-2018.
 */
public class SearchItem {

    public String id;

    public String thumbnailImageUrl;

    public String videoUrl;

    public LikeDislikeCounter likeDislikeCounter;

    public String videoName;

    public int getCountOfLikes() {
        return likeDislikeCounter != null ? likeDislikeCounter.countOfLike : 0;
    }

    public int getCountOfDisLikes() {
        return likeDislikeCounter != null ? likeDislikeCounter.countOfDisLike : 0;
    }

    public void increaseLikesCount() {
        if (likeDislikeCounter != null) {
            likeDislikeCounter.countOfLike++;
        }
    }

    public void increaseDisLikesCount() {
        if (likeDislikeCounter != null) {
            likeDislikeCounter.countOfDisLike++;
        }
    }
}
