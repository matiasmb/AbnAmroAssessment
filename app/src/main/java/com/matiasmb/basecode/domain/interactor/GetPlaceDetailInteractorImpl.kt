package com.matiasmb.basecode.domain.interactor

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import com.matiasmb.basecode.data.dto.Location
import com.matiasmb.basecode.data.dto.Photo
import com.matiasmb.basecode.data.dto.Place
import com.matiasmb.basecode.data.repository.PlacesRepository
import com.matiasmb.basecode.data.service.PlacesApiService
import com.matiasmb.basecode.data.service.ResponseType
import com.matiasmb.basecode.domain.model.PlaceView
import com.matiasmb.basecode.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetPlaceDetailInteractorImpl @Inject constructor(
    private val placesRepository: PlacesRepository
) : GetPlaceDetailInteractor {

    override suspend fun fetchPlaceDetails(placeId: String): Flow<Resource<PlaceView?>> {
        return flow {
            placesRepository.getPlacesDetail(placeId).collect{
                emit(when (it) {
                    is Resource.Error -> Resource.Error(it.error, transformResult(it.data))
                    is Resource.Success -> Resource.Success(transformResult(it.data))
                })
            }
        }
    }

    @VisibleForTesting (otherwise = PRIVATE)
    fun transformResult(place: Place?): PlaceView? {
        return place?.let {
            PlaceView(
                name = place.name,
                description = place.description,
                formattedAddress = getFormattedAddress(place.location),
                imageUrl = getMainPhoto(place.photos),
                contactInformation = getContactInformation(place.email, place.tel, place.website),
                rating = place.rating
            )
        }
    }

    @VisibleForTesting (otherwise = PRIVATE)
    fun getContactInformation(email: String?, tel: String?, website: String?): String {
        var contactInformation = ""
        tel?.let {
            contactInformation += "Telephone: ${it}\n"
        }
        email?.let {
            contactInformation += "Email: ${it}\n"
        }
        website?.let {
            contactInformation += "Website: $it"
        }
        return contactInformation
    }

    @VisibleForTesting (otherwise = PRIVATE)
    fun getFormattedAddress(location: Location): String {
        return "Address: ${location.address}\nPostal code: ${location.postcode}\nCity: ${location.locality}"
    }

    @VisibleForTesting (otherwise = PRIVATE)
    fun getMainPhoto(photos: List<Photo>?): String? {
        return if (photos.isNullOrEmpty()) {
            null
        } else {
            "${photos[0].prefix}original${photos[0].suffix}"
        }
    }
}
