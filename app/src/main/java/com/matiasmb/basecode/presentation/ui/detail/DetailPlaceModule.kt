package com.matiasmb.basecode.presentation.ui.detail

import dagger.Module
import dagger.Provides

@Module
class DetailPlaceModule {

    @Provides
    fun provideDetailFragment(fragment: DetailFragment): DetailPlaceViewState {
        val args = DetailFragmentArgs.fromBundle(fragment.requireArguments())
        return DetailPlaceViewState(args.placeId, DetailPlaceViewState.ViewStateContent.Initial)
    }
}
