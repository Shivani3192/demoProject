package com.example.spatel.giphyexample.searchvideo.videolisting;

import com.example.spatel.giphyexample.searchvideo.model.SearchItem;

/**
 * Helps us track the relationship between the View and the Presenter in a central place.
 * <p>
 * Created by spatel on 01-04-2018.
 */
public class SearchVideoContract {

    /**
     * Represents the View in MVP.
     */
   public interface View {
        void showProgressBar();

        void hideProgressBar();

        void displayVideos();

        void notifyError(String errorMsg);

        void hideKeyboard();

        void clearRecyclerview();
    }

    /**
     * Represents the Presenter in MVP.
     */
    public interface Presenter {
        void loadVideos(String keyword);

        void notifyIncrementOfLikeCounterToDb(SearchItem searchItem);

        void notifyIncrementOfDisLikeCounterToDb(SearchItem searchItem);
    }
}
