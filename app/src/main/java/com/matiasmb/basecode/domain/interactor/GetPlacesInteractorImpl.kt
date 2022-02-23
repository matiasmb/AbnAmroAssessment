package com.matiasmb.basecode.domain.interactor

import androidx.annotation.VisibleForTesting
import com.matiasmb.basecode.data.dto.Location
import com.matiasmb.basecode.data.dto.Place
import com.matiasmb.basecode.data.service.PlacesApiService
import com.matiasmb.basecode.data.service.ResponseType
import com.matiasmb.basecode.domain.model.ItemPlaceView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetPlacesInteractorImpl @Inject constructor(
    private val placesApiService: PlacesApiService
) : GetPlacesInteractor {

    override suspend fun fetchPlaces(nearLocation: String): Flow<List<ItemPlaceView>?> {
        return flow {
            placesApiService.getPlacesByNearLocation(nearLocation).collect {
                when(it) {
                    ResponseType.Failure -> emit(null)
                    is ResponseType.Success -> emit(transformResult(it.data.results))
                }
            }
        }
    }

    @VisibleForTesting (otherwise = VisibleForTesting.PRIVATE)
    fun transformResult(placeList: List<Place>): List<ItemPlaceView> {
        return placeList.map { place ->
            ItemPlaceView(
                place.id,
                place.name,
                getFormattedAddress(place.location)
            )
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getFormattedAddress(location: Location): String {
        return "Address: ${location.address}\nPostal code: ${location.postcode}\nCity: ${location.locality}"
    }
}
