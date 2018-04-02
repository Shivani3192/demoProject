package com.example.spatel.giphyexample.searchvideo.videolisting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.example.spatel.giphyexample.GiphyApp;
import com.example.spatel.giphyexample.R;
import com.example.spatel.giphyexample.searchvideo.fullvideo.FullVideoController;
import com.example.spatel.giphyexample.searchvideo.model.SearchItem;
import com.example.spatel.giphyexample.searchvideo.videolisting.adapter.VideoListingAdapter;
import com.example.spatel.giphyexample.utils.Constant;
import com.example.spatel.giphyexample.utils.LogUtils;
import com.example.spatel.giphyexample.utils.Utils;
import com.example.spatel.giphyexample.utils.customviews.EqualSpacingItemDecoration;
import com.example.spatel.giphyexample.utils.ui.BaseController;
import com.giphy.sdk.core.network.api.GPHApiClient;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;

/**
 * Created by spatel on 01-04-2018.
 */
public class SearchVideoController extends BaseController implements SearchVideoContract.View, VideoListingAdapter.VideoListingAdapterListener {

    private static final int COLUMN_COUNT = 2;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    View progressBar;

    @BindView(R.id.search_video_edittext)
    EditText searchVideoEditText;

    @BindString(R.string.search)
    String title;

    @BindString(R.string.giphy_api_key)
    String giphyApiKey;

    @BindDimen(R.dimen.size_5)
    int size_5;

    @Inject
    SearchVideoPresenter presenter;

    private VideoListingAdapter mVideoListingAdapter;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_search_video, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        initializePresenter();
        initializeViews(view);
    }

    /**
     * initialize presenter after view created
     *
     */
    private void initializePresenter() {
        GPHApiClient client = new GPHApiClient(giphyApiKey);
        // Creates presenter
        DaggerSearchVideoComponent.builder()
                .searchVideoPresenterModule(new SearchVideoPresenterModule(this, client,
                        ((GiphyApp) getActivity().getApplication()).getBoxStore()))
                .build()
                .inject(this);

    }

    /**
     * initialize views
     *
     * @param view
     */
    private void initializeViews(View view) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), COLUMN_COUNT));
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(size_5));
        searchVideoEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchVideoEditText.getText() != null &&
                            !TextUtils.isEmpty(searchVideoEditText.getText().toString().trim())) {
                        performSearch();
                    } else {
                        clearRecyclerview();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * clear list and adapter
     *
     */
    @Override
    public void clearRecyclerview() {
        presenter.mSearchItemList.clear();
        mVideoListingAdapter = null;
        setAdapter();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        if (presenter.mSearchItemList != null) {
            setAdapter();
        }
    }

    private void performSearch() {
        presenter.loadVideos(searchVideoEditText.getText().toString().trim());
    }

    @Override
    protected String getTitle() {
        return title;
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void displayVideos() {
        setAdapter();
    }

    @Override
    public void notifyError(String errorMsg) {
        Snackbar.make(recyclerView, "Error: " + errorMsg, Snackbar.LENGTH_LONG);
    }

    @Override
    public void hideKeyboard() {
        Utils.hideSoftKeyboard(searchVideoEditText);
    }

    private void setAdapter() {
        LogUtils.I(SearchVideoController.class.getSimpleName(), "setAdapter");
        if (mVideoListingAdapter == null) {
            mVideoListingAdapter = new VideoListingAdapter(presenter.mSearchItemList, this);
            recyclerView.setAdapter(mVideoListingAdapter);
        } else if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(mVideoListingAdapter);
        } else {
            mVideoListingAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onVideoLikeButtonClick(SearchItem searchItem) {
        presenter.notifyIncrementOfLikeCounterToDb(searchItem);
    }

    @Override
    public void onVideoDisLikeButtonClick(SearchItem searchItem) {
        presenter.notifyIncrementOfLikeCounterToDb(searchItem);
    }

    @Override
    public void onItemClick(SearchItem searchItem) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BundleKeysName.videoUrl, searchItem.videoUrl);
        bundle.putString(Constant.BundleKeysName.videoName, searchItem.videoName);
        getRouter().pushController(
                RouterTransaction.with(new FullVideoController(bundle))
                        .pushChangeHandler(new FadeChangeHandler())
                        .popChangeHandler(new FadeChangeHandler()));

    }
}
