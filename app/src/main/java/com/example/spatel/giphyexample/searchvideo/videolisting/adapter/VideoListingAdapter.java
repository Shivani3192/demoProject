package com.example.spatel.giphyexample.searchvideo.videolisting.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.spatel.giphyexample.R;
import com.example.spatel.giphyexample.searchvideo.model.SearchItem;
import com.example.spatel.giphyexample.utils.ImageUtils;
import com.example.spatel.giphyexample.utils.customviews.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by spatel on 01-04-2018.
 */
public class VideoListingAdapter extends RecyclerView.Adapter<VideoListingAdapter.ViewHolder> implements View.OnClickListener {

    public interface VideoListingAdapterListener {
        void onVideoLikeButtonClick(SearchItem searchItem);

        void onVideoDisLikeButtonClick(SearchItem searchItem);

        void onItemClick(SearchItem searchItem);
    }

    private List<SearchItem> mSearchItems;
    private VideoListingAdapterListener mVideoListingAdapterListener;

    public VideoListingAdapter(List<SearchItem> searchItems, VideoListingAdapterListener videoListingAdapterListener) {
        mSearchItems = searchItems;
        mVideoListingAdapterListener = videoListingAdapterListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.likesCounterTextview.setText(String.valueOf(mSearchItems.get(position).getCountOfLikes()));
        holder.dislikeCounterTextview.setText(String.valueOf(mSearchItems.get(position).getCountOfDisLikes()));
        ImageUtils.loadImage(holder.imageView.getContext(), mSearchItems.get(position).thumbnailImageUrl, holder.imageView);
        holder.likeImageButton.setTag(position);
        holder.likeImageButton.setOnClickListener(this);
        holder.disLikeImageButton.setTag(position);
        holder.disLikeImageButton.setOnClickListener(this);
        holder.rootLayout.setTag(position);
        holder.rootLayout.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mSearchItems != null ? mSearchItems.size() : 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.like_imagebutton:
                mSearchItems.get((Integer) view.getTag()).increaseLikesCount();
                notifyItemChanged((Integer) view.getTag());
                mVideoListingAdapterListener.onVideoLikeButtonClick(mSearchItems.get((Integer) view.getTag()));
                break;
            case R.id.dislike_imagebutton:
                mSearchItems.get((Integer) view.getTag()).increaseDisLikesCount();
                notifyItemChanged((Integer) view.getTag());
                mVideoListingAdapterListener.onVideoDisLikeButtonClick(mSearchItems.get((Integer) view.getTag()));
                break;
            case R.id.root_layout:
                mVideoListingAdapterListener.onItemClick(mSearchItems.get((Integer) view.getTag()));
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout)
        LinearLayout rootLayout;

        @BindView(R.id.thumbnail_image_view)
        SquareImageView imageView;

        @BindView(R.id.like_layout)
        LinearLayout likesLayout;

        @BindView(R.id.dislike_layout)
        LinearLayout disLikeLayout;

        @BindView(R.id.likes_counter_textview)
        TextView likesCounterTextview;

        @BindView(R.id.dislike_counter_textview)
        TextView dislikeCounterTextview;

        @BindView(R.id.like_imagebutton)
        ImageButton likeImageButton;

        @BindView(R.id.dislike_imagebutton)
        ImageButton disLikeImageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
