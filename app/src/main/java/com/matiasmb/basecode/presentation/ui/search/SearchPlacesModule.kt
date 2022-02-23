package com.matiasmb.basecode.presentation.ui.search

import dagger.Module
import dagger.Provides

@Module
class SearchPlacesModule {

    @Provides
    fun provideSearchPlacesFragment(): SearchPlacesViewState {
        return SearchPlacesViewState(SearchPlacesViewState.ViewStateContent.Initial)
    }
}
