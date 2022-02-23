package com.matiasmb.basecode.data.di

import androidx.room.Room
import com.google.gson.GsonBuilder
import com.matiasmb.basecode.BuildConfig
import com.matiasmb.basecode.MyApplication
import com.matiasmb.basecode.data.FoursquareApiClient
import com.matiasmb.basecode.data.repository.PlacesRepository
import com.matiasmb.basecode.database.PlaceDatabase
import com.matiasmb.basecode.data.repository.PlacesRepositoryImpl
import com.matiasmb.basecode.data.service.PlacesApiService
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
class NetworkingModule {

    @Provides
    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.addInterceptor(headerInterceptor)

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(logging)

        return builder.build()
    }

    @Provides
    fun provideItemsApiClient(httpClient: Lazy<OkHttpClient>): FoursquareApiClient {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient.get())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(FoursquareApiClient::class.java)
    }

    @Provides
    fun provideDatabase(app: MyApplication): PlaceDatabase =
        Room.databaseBuilder(app, PlaceDatabase::class.java, "PlacesDatabase").build()

    @Provides
    fun provideRepository(
        placesApiService: PlacesApiService,
        placeDatabase: PlaceDatabase
    ): PlacesRepository {
        return PlacesRepositoryImpl(placesApiService, placeDatabase)
    }
}
