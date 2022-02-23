package com.matiasmb.basecode.domain.interactor

import com.matiasmb.basecode.domain.model.PlaceView
import com.matiasmb.basecode.util.Resource
import kotlinx.coroutines.flow.Flow

interface GetPlaceDetailInteractor {

    suspend fun fetchPlaceDetails(placeId: String): Flow<Resource<PlaceView?>>
}
