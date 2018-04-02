package com.example.spatel.giphyexample.searchvideo.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by spatel on 01-04-2018.
 */
@Entity
public class LikeDislikeCounter {
    @Id
    public long id;

    public String parentId;

    public int countOfLike;

    public int countOfDisLike;
}
