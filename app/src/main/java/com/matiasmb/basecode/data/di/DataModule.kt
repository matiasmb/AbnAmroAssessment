package com.matiasmb.basecode.data.di

import com.matiasmb.basecode.data.FoursquareApiClient
import com.matiasmb.basecode.data.service.PlacesApiService
import com.matiasmb.basecode.data.service.PlacesApiServiceImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
class DataModule {

    @ExperimentalCoroutinesApi
    @Provides
    fun provideItemsApiService(apiClient: FoursquareApiClient): PlacesApiService {
        return PlacesApiServiceImpl(apiClient)
    }
}
