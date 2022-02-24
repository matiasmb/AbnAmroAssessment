package com.matiasmb.basecode.data.service

import com.matiasmb.basecode.data.dto.Place

interface PlacesApiService {

    suspend fun getPlacesByNearLocation(nearLocation: String): List<Place>

    suspend fun getPlacesDetails(placeId: String): Place
}
