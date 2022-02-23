package com.matiasmb.basecode.domain.interactor

import com.matiasmb.basecode.domain.model.ItemPlaceView
import kotlinx.coroutines.flow.Flow

interface GetPlacesInteractor {

    suspend fun fetchPlaces(nearLocation: String): Flow<List<ItemPlaceView>?>
}
