package com.example.spatel.giphyexample.customviews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.spatel.giphyexample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spatel on 25-03-2018.
 */

public abstract class EndlessRecyclerviewFragment<T> extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        Response.ErrorListener, Response.Listener<List<T>> {

    public static final int DEFAULT_INDEX = 1;
    protected RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeToRefreshLayout;
    protected List<T> mList = new ArrayList<>();
    protected View mProgressBar;
    private int mCurrentIndex;
    protected EndlessRecyclerviewAdapter<T> mAdapter;
    private boolean mRefreshing = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mLayoutManager = getLayoutManager();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(getItemDecoration()));
        mSwipeToRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh_layout);
        mSwipeToRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(mEndlessScrollListener);
    }

    private EndlessRecyclerOnScrollListener mEndlessScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore(final int current_page) {
            if (!mRefreshing) {
                mEndlessScrollListener.setLoading(true);
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Notify adapter with appropriate notify methods
                        mList.add(null);
                        mAdapter.notifyItemInserted(mList.size()-1);
                        getListItem(current_page);
                    }
                });
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (mList == null || mList.isEmpty()) {
            getListItem(mCurrentIndex);
        } else {
            setAdapter();
        }
    }

    @Override
    public void onRefresh() {
        mRefreshing = true;
        mCurrentIndex = DEFAULT_INDEX;
        getListItem(mCurrentIndex);
        mEndlessScrollListener.reset();
    }

    protected void getListItem(int currentIndex) {
        mCurrentIndex = currentIndex;
        if (currentIndex == DEFAULT_INDEX) {
            mSwipeToRefreshLayout.setRefreshing(false);
            mProgressBar.setVisibility(View.VISIBLE);
            if (mList != null) {
                mList.clear();
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
        downloadListItem(currentIndex);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mRefreshing = false;
        mProgressBar.setVisibility(View.GONE);
        mEndlessScrollListener.setLoading(false);
        if (!mList.isEmpty()) {
            mList.remove(mList.size() - 1);
            mAdapter.notifyItemRangeRemoved(mList.size()-1, mList.size());
        }
        Log.i(EndlessRecyclerviewFragment.class.getSimpleName(), error.getMessage());
    }

    @Override
    public void onResponse(List<T> response) {
        mRefreshing = false;
        mProgressBar.setVisibility(View.GONE);
        mEndlessScrollListener.setLoading(false);
        if (!mList.isEmpty()) {
            mList.remove(mList.size() - 1);
            mAdapter.notifyDataSetChanged();
        }
        if (response != null) {
            mList.addAll(response);
        }
        setAdapter();
    }

    protected abstract void downloadListItem(int currentIndex);

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract void setAdapter();

    protected int getItemDecoration() {
        return 20;
    }
}
