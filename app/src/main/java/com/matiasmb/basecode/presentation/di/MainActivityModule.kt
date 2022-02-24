package com.matiasmb.basecode.presentation.di

import com.matiasmb.basecode.PerFragment
import com.matiasmb.basecode.presentation.ui.detail.DetailFragment
import com.matiasmb.basecode.presentation.ui.detail.DetailPlaceModule
import com.matiasmb.basecode.presentation.ui.search.SearchPlacesFragment
import com.matiasmb.basecode.presentation.ui.search.SearchPlacesModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [SearchPlacesModule::class])
    abstract fun homeFragmentInjector(): SearchPlacesFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [DetailPlaceModule::class])
    abstract fun detailPlaceFragmentInjector(): DetailFragment
}
