package com.example.spatel.giphyexample.searchvideo.fullvideo;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.spatel.giphyexample.R;
import com.example.spatel.giphyexample.utils.Constant;
import com.example.spatel.giphyexample.utils.ui.BaseController;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by spatel on 01-04-2018.
 */
public class FullVideoController extends BaseController {

    private String mTitle;
    private String mVideoUrl;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    private ImageView ivHideControllerButton;

    public FullVideoController(Bundle bundle) {
        mVideoUrl = bundle.getString(Constant.BundleKeysName.videoUrl);
        mTitle = bundle.getString(Constant.BundleKeysName.videoName);
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_full_video, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();
        ivHideControllerButton = view.findViewById(R.id.exo_controller);

        simpleExoPlayerView = view.findViewById(R.id.player_view);
    }

    private void initializePlayer() {

        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mVideoUrl),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);

//        ivHideControllerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                simpleExoPlayerView.hideController();
//            }
//        });
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    protected String getTitle() {
        return mTitle;
    }
}
