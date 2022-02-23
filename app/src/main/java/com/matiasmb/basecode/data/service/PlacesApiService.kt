package com.matiasmb.basecode.data.service

import com.matiasmb.basecode.data.dto.Place
import com.matiasmb.basecode.data.dto.PlaceSearchResponse
import kotlinx.coroutines.flow.Flow

interface PlacesApiService {

    suspend fun getPlacesByNearLocation(nearLocation: String): List<Place>

    suspend fun getPlacesDetails(placeId: String): Place
}
