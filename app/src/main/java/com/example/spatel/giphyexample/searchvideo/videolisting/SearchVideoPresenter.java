package com.example.spatel.giphyexample.searchvideo.videolisting;

import com.example.spatel.giphyexample.searchvideo.model.LikeDislikeCounter;
import com.example.spatel.giphyexample.searchvideo.model.LikeDislikeCounter_;
import com.example.spatel.giphyexample.searchvideo.model.SearchItem;
import com.example.spatel.giphyexample.utils.LogUtils;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.response.ListMediaResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by spatel on 01-04-2018.
 */
public class SearchVideoPresenter implements SearchVideoContract.Presenter {

    private SearchVideoContract.View mView;
    private GPHApi mGphApi;
    public List<SearchItem> mSearchItemList = new ArrayList<>();
    private BoxStore mBoxStore;
    private Box<LikeDislikeCounter> likeDislikeCounterBox;

    /**
     * class constructor
     *
     * @param view
     * @param gphApi
     * @param boxStore
     */
    @Inject
    public SearchVideoPresenter(SearchVideoContract.View view, GPHApi gphApi, BoxStore boxStore) {
        this.mView = view;
        this.mGphApi = gphApi;
        this.mBoxStore = boxStore;
    }

    /**
     * search videos according to keyword
     *
     * @param keyword
     */
    @Override
    public void loadVideos(String keyword) {
        mView.showProgressBar();
        mView.clearRecyclerview();
        mView.hideKeyboard();
        /// Gif Search
        mGphApi.search(keyword, MediaType.gif, null, null, null,
                null, new CompletionHandler<ListMediaResponse>() {
                    @Override
                    public void onComplete(ListMediaResponse result, Throwable e) {
                        if (result == null) {
                            // Do what you want to do with the error
                            mView.notifyError("No results found");
                        } else {
                            if (result.getData() != null) {
                                processData(result.getData());
                            } else {
                                mView.notifyError("No results found");
                            }
                        }
                    }
                });
    }

    /**
     * update like counter into database
     *
     * @param searchItem
     */
    @Override
    public void notifyIncrementOfLikeCounterToDb(SearchItem searchItem) {
        initializeLikeDisLikeCounterBox();
        likeDislikeCounterBox.put(searchItem.likeDislikeCounter);
    }

    /**
     * initialize LikeDisLikeCounter table
     */
    private void initializeLikeDisLikeCounterBox() {
        if (likeDislikeCounterBox == null) {
            likeDislikeCounterBox = mBoxStore.boxFor(LikeDislikeCounter.class);
        }
    }

    /**
     * update dislike counter into database
     *
     * @param searchItem
     */
    @Override
    public void notifyIncrementOfDisLikeCounterToDb(SearchItem searchItem) {
        initializeLikeDisLikeCounterBox();
        likeDislikeCounterBox.put(searchItem.likeDislikeCounter);
    }

    /**
     * convert Media model to SearchItem and add like dislike counter if exists in database
     * @param data
     */
    private void processData(List<Media> data) {
        if (!data.isEmpty()) {
            likeDislikeCounterBox = mBoxStore.boxFor(LikeDislikeCounter.class);
            Observable.fromIterable(data)
                    .flatMap(new Function<Media, ObservableSource<SearchItem>>() {
                        @Override
                        public ObservableSource<SearchItem> apply(Media media) throws Exception {
                            LikeDislikeCounter likeDislikeCounter =
                                    likeDislikeCounterBox.query().equal(LikeDislikeCounter_.parentId, media.getId())
                                            .build().findFirst();
                            SearchItem searchItem = new SearchItem();
                            searchItem.id = media.getId();
                            searchItem.videoName = media.getTitle();
                            searchItem.thumbnailImageUrl = media.getImages().getFixedWidthStill()
                                    .getGifUrl();
                            searchItem.videoUrl = media.getImages().getFixedWidth().getMp4Url();
                            if (likeDislikeCounter == null) {
                                likeDislikeCounter = new LikeDislikeCounter();
                                likeDislikeCounter.parentId = media.getId();
                            }
                            searchItem.likeDislikeCounter = likeDislikeCounter;
                            searchItem.likeDislikeCounter.countOfLike = likeDislikeCounter.countOfLike;
                            searchItem.likeDislikeCounter.countOfDisLike = likeDislikeCounter.countOfDisLike;
                            return Observable.just(searchItem);
                        }
                    }).toList()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<SearchItem>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<SearchItem> searchItems) {
                            mSearchItemList.addAll(searchItems);
                            LogUtils.I(SearchVideoPresenter.class.getSimpleName(), mSearchItemList.get(0).videoName);
                            mView.hideProgressBar();
                            mView.displayVideos();
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.I(SearchVideoPresenter.class.getSimpleName(), e.getMessage());
                            mView.hideProgressBar();
                            mView.notifyError(e.getMessage());
                        }
                    });

        } else {
            mView.notifyError("No results found");
        }
    }
}
