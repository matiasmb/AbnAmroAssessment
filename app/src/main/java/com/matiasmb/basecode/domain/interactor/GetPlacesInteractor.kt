package com.matiasmb.basecode.domain.interactor

import com.matiasmb.basecode.domain.model.ItemPlaceView
import com.matiasmb.basecode.util.Resource
import kotlinx.coroutines.flow.Flow

interface GetPlacesInteractor {

    suspend fun fetchPlaces(nearLocation: String): Flow<Resource<List<ItemPlaceView>?>>
}
