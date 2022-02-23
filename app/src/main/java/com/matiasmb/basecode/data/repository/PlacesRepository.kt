package com.matiasmb.basecode.data.repository

import com.matiasmb.basecode.data.dto.Place
import com.matiasmb.basecode.util.Resource
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    fun getPlacesByNearLocation(nearLocation: String): Flow<Resource<List<Place>>>

    fun getPlacesDetail(placeId: String): Flow<Resource<Place>>
}
