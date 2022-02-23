package com.matiasmb.basecode.data.networking

import com.matiasmb.basecode.TestData
import com.matiasmb.basecode.data.FoursquareApiClient
import com.matiasmb.basecode.data.dto.Place
import com.matiasmb.basecode.data.dto.PlaceSearchResponse
import com.matiasmb.basecode.data.service.PlacesApiServiceImpl
import com.matiasmb.basecode.data.service.ResponseType
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class PlacesApiServiceImplTest {

    private lateinit var placesApiService: PlacesApiServiceImpl

    @Test
    fun `searchPlaces WHEN FoursquareApiClient give a successful response SHOULD return a non empty list of places inside the response`() {
        runBlocking {
            // GIVEN
            val apiClient = mock<FoursquareApiClient> {
                onBlocking {
                    searchPlaces(
                        anyString()
                    )
                } doReturn TestData.dataPlaceSearchResponse
            }
            placesApiService = PlacesApiServiceImpl(apiClient)

            //WHEN
            val response: Flow<ResponseType<PlaceSearchResponse>> =
                placesApiService.getPlacesByNearLocation("Rotterdam")

            //THEN
            response.collect {
                assertTrue(it is ResponseType.Success)
                assertTrue((it as ResponseType.Success).data.results.isNotEmpty())
            }
        }
    }

    @Test
    fun `searchProducts WHEN FoursquareApiClient give a successful response SHOULD return a Failure Type`() {
        runBlocking {
            // GIVEN
            val apiClient = mock<FoursquareApiClient> {
                onBlocking {
                    searchPlaces(
                        anyString()
                    )
                } doReturn TestData.dataPlaceSearchResponseEmpty
            }
            placesApiService = PlacesApiServiceImpl(apiClient)

            //WHEN
            val response: Flow<ResponseType<PlaceSearchResponse>> = placesApiService.getPlacesByNearLocation("Rotterdam")

            //THEN
            response.collect {
                assertTrue(it is ResponseType.Failure)
            }
        }
    }

    @Test
    fun `getPlaceDetails WHEN FoursquareApiClient give a successful response SHOULD return a place detail`() {
        runBlocking {
            // GIVEN
            val mockId = "5b6a9a644ac28a002cf131d0"
            val apiClient = mock<FoursquareApiClient> {
                onBlocking {
                    getPlaceDetails(
                        anyString()
                    )
                } doReturn TestData.placeDetailSearch
            }
            placesApiService = PlacesApiServiceImpl(apiClient)

            //WHEN
            val response: Flow<ResponseType<Place>> =
                placesApiService.getPlacesDetails(mockId)

            //THEN
            response.collect {
                assertTrue(it is ResponseType.Success)
                assertTrue((it as ResponseType.Success).data.id == mockId)
                assertTrue(it.data.photos?.isNotEmpty() == true)
            }
        }
    }
}
