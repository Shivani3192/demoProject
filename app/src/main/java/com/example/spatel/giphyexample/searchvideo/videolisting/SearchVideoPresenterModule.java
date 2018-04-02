package com.example.spatel.giphyexample.searchvideo.videolisting;

import com.giphy.sdk.core.network.api.GPHApi;

import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;

/**
 * Created by spatel on 01-04-2018.
 */
@Module
public class SearchVideoPresenterModule {

    private final SearchVideoContract.View view;

    private final GPHApi gphApi;

    private final BoxStore boxStore;

    public SearchVideoPresenterModule(SearchVideoContract.View view, GPHApi gphApi, BoxStore boxStore) {
        this.view = view;
        this.gphApi = gphApi;
        this.boxStore = boxStore;
    }

    @Provides
    SearchVideoContract.View provideSearchVideoContractView() {
        return view;
    }

    @Provides
    GPHApi provideGPHApi() {
        return gphApi;
    }

    @Provides
    BoxStore provideBoxStore() {
        return boxStore;
    }

}
