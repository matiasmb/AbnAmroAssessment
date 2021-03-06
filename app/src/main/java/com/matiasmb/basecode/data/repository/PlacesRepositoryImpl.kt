package com.matiasmb.basecode.data.repository

import androidx.room.withTransaction
import com.matiasmb.basecode.data.dao.PlacesDao
import com.matiasmb.basecode.data.service.PlacesApiService
import com.matiasmb.basecode.database.PlaceDatabase
import com.matiasmb.basecode.util.networkBoundResource
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesApiService: PlacesApiService,
    private val placesDatabase: PlaceDatabase
    ): PlacesRepository {

    private val placesDao: PlacesDao = placesDatabase.placesDao()

    override fun getPlacesByNearLocation(nearLocation: String) = networkBoundResource(
        query = {
            placesDao.getPlacesByNearLocation(nearLocation)
        },
        fetch = {
            placesApiService.getPlacesByNearLocation(nearLocation)
        },
        saveFetchResult = { places ->
            placesDatabase.withTransaction {
                placesDao.insertPlaces(places)
            }
        }
    )

    override fun getPlacesDetail(placeId: String) = networkBoundResource(
        query = {
            placesDao.getPlacesDetails(placeId)
        },
        fetch = {
            placesApiService.getPlacesDetails(placeId)
        },
        saveFetchResult = { places ->
            placesDatabase.withTransaction {
                placesDao.insertPlace(places)
            }
        }
    )
}
