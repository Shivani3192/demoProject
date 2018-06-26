package com.example.spatel.giphyexample.customviews.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikunjrapariya.interview.customviews.EndlessRecyclerviewFragment;
import com.example.nikunjrapariya.interview.manager.MainActivityManager;
import com.example.nikunjrapariya.interview.model.ContactItem;
import com.example.spatel.giphyexample.R;

/**
 * Created by spatel on 25-03-2018.
 */

public class ContactFragment extends EndlessRecyclerviewFragment<ContactItem> implements ContactEndlessAdapter.ContactEndlessAdapterListener {

    public interface ContactFragmentListener {
        void onContactClick(ContactItem contactItem);
    }

    private ContactFragmentListener mContactFragmentListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContactFragmentListener = (ContactFragmentListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContactFragmentListener = (ContactFragmentListener) activity;
    }

    @Override
    protected void downloadListItem(int currentIndex) {
        Log.i(ContactFragment.class.getSimpleName(), "currentIndex: " + currentIndex);
        MainActivityManager.getContactList(getActivity().getApplication(), currentIndex, ContactFragment.class.getName(),
                this, this);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    protected void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new ContactEndlessAdapter(mList, this);
            mRecyclerView.setAdapter(mAdapter);
        } else if (mRecyclerView.getAdapter() == null) {
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(ContactItem contactItem) {
        mContactFragmentListener.onContactClick(contactItem);
    }
}
