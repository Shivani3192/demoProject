package com.example.spatel.giphyexample.searchvideo.videolisting;

import dagger.Component;

/**
 * Created by spatel on 01-04-2018.
 */
@Component(modules = SearchVideoPresenterModule.class)
public interface SearchVideoComponent {

    void inject(SearchVideoController searchVideoController);
}
