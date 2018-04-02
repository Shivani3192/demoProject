package com.example.spatel.giphyexample.searchvideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.example.spatel.giphyexample.R;
import com.example.spatel.giphyexample.searchvideo.videolisting.SearchVideoController;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.controller_container)
    ViewGroup container;

    private Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setToolBar();

        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new SearchVideoController()));
        }
    }

    /**
     *
     * set toolbar as a actionbar and back button
     */
    private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }

    /**
     * allow toolbar's back button act as a actionbar back button
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
