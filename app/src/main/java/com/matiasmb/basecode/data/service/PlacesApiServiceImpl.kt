package com.matiasmb.basecode.data.service

import com.matiasmb.basecode.data.FoursquareApiClient
import com.matiasmb.basecode.data.dto.Place
import com.matiasmb.basecode.data.dto.PlaceSearchResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class PlacesApiServiceImpl @Inject constructor(
    private val apiClient: FoursquareApiClient
) : PlacesApiService {

    override suspend fun getPlacesByNearLocation(nearLocation: String): Flow<ResponseType<PlaceSearchResponse>> {
        return flow {
            apiClient.searchPlaces(nearLocation).takeIf { it.results.isNotEmpty() }?.let {
                emit(ResponseType.Success(it))
            } ?: run{
                ResponseType.Failure
            }
        }.catch {
            ResponseType.Failure
        }
    }

    override suspend fun getPlacesDetails(placeId: String): Flow<ResponseType<Place>> {
        return flow {
            apiClient.getPlaceDetails(placeId). run {
                emit(ResponseType.Success(this))
            }
        }.catch {
            ResponseType.Failure
        }
    }
}
