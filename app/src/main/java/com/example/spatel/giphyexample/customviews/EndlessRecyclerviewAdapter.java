package com.example.spatel.giphyexample.customviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikunjrapariya.interview.R;

import java.util.List;

/**
 * Created by spatel on 25-03-2018.
 */

public abstract class EndlessRecyclerviewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_LOADING = 1;
    private static final int VIEW_TYPE_ITEM = 0;
    protected List<T> mList;

    public EndlessRecyclerviewAdapter(List<T> list) {
        mList = list;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return getEndlessViewHolder(parent);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    protected abstract RecyclerView.ViewHolder getEndlessViewHolder(ViewGroup parent);

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View view) {
            super(view);
        }
    }
}
