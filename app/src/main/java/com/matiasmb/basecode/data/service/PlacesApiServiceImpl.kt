package com.matiasmb.basecode.data.service

import com.matiasmb.basecode.data.FoursquareApiClient
import com.matiasmb.basecode.data.dto.Place
import javax.inject.Inject

class PlacesApiServiceImpl @Inject constructor(
    private val apiClient: FoursquareApiClient
) : PlacesApiService {

    override suspend fun getPlacesByNearLocation(nearLocation: String): List<Place> {
        return apiClient.searchPlaces(nearLocation).results
    }

    override suspend fun getPlacesDetails(placeId: String): Place {
        return apiClient.getPlaceDetails(placeId)
    }
}
