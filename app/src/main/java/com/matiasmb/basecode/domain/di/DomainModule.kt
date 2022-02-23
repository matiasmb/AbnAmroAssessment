package com.matiasmb.basecode.domain.di

import com.matiasmb.basecode.data.service.PlacesApiService
import com.matiasmb.basecode.domain.interactor.GetPlaceDetailInteractor
import com.matiasmb.basecode.domain.interactor.GetPlaceDetailInteractorImpl
import com.matiasmb.basecode.domain.interactor.GetPlacesInteractor
import com.matiasmb.basecode.domain.interactor.GetPlacesInteractorImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
class DomainModule {

    @ExperimentalCoroutinesApi
    @Provides
    fun provideGetPlacesInteractor(placesApiService: PlacesApiService): GetPlacesInteractor {
        return GetPlacesInteractorImpl(placesApiService)
    }

    @ExperimentalCoroutinesApi
    @Provides
    fun provideGetPlaceDetailsInteractor(placesApiService: PlacesApiService): GetPlaceDetailInteractor {
        return GetPlaceDetailInteractorImpl(placesApiService)
    }
}
