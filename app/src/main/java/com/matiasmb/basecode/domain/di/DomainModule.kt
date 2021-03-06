package com.matiasmb.basecode.domain.di

import com.matiasmb.basecode.data.repository.PlacesRepositoryImpl
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
    fun provideGetPlacesInteractor(placesRepository: PlacesRepositoryImpl): GetPlacesInteractor {
        return GetPlacesInteractorImpl(placesRepository)
    }

    @ExperimentalCoroutinesApi
    @Provides
    fun provideGetPlaceDetailsInteractor(placesRepository: PlacesRepositoryImpl): GetPlaceDetailInteractor {
        return GetPlaceDetailInteractorImpl(placesRepository)
    }
}
