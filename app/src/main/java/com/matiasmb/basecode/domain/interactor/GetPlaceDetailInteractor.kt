package com.matiasmb.basecode.domain.interactor

import com.matiasmb.basecode.domain.model.PlaceView
import kotlinx.coroutines.flow.Flow

interface GetPlaceDetailInteractor {

    suspend fun fetchPlaceDetails(placeId: String): Flow<PlaceView?>
}
