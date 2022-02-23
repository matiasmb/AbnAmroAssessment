package com.matiasmb.basecode.domain.interactor

import androidx.annotation.VisibleForTesting
import com.matiasmb.basecode.data.dto.Location
import com.matiasmb.basecode.data.dto.Place
import com.matiasmb.basecode.data.repository.PlacesRepository
import com.matiasmb.basecode.data.service.PlacesApiService
import com.matiasmb.basecode.data.service.ResponseType
import com.matiasmb.basecode.domain.model.ItemPlaceView
import com.matiasmb.basecode.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetPlacesInteractorImpl @Inject constructor(
    private val placesRepository: PlacesRepository
) : GetPlacesInteractor {

    override suspend fun fetchPlaces(nearLocation: String): Flow<Resource<List<ItemPlaceView>?>> {
        return flow {
            placesRepository.getPlacesByNearLocation(nearLocation).collect{
                emit(when (it) {
                    is Resource.Error -> Resource.Error(it.error, transformResult(it.data))
                    is Resource.Success -> Resource.Success(transformResult(it.data))
                })
            }
        }
    }

    @VisibleForTesting (otherwise = VisibleForTesting.PRIVATE)
    fun transformResult(placeList: List<Place>?): List<ItemPlaceView>? {
        return placeList?.map { place ->
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
