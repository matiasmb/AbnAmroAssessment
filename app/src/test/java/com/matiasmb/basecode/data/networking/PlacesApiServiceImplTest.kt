package com.matiasmb.basecode.data.networking

import com.matiasmb.basecode.TestData
import com.matiasmb.basecode.data.FoursquareApiClient
import com.matiasmb.basecode.data.dto.Place
import com.matiasmb.basecode.data.service.PlacesApiServiceImpl
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class PlacesApiServiceImplTest {

    private lateinit var placesApiService: PlacesApiServiceImpl

    @Test
    fun `getPlacesByNearLocation WHEN FoursquareApiClient give a successful response SHOULD return a non empty list of places inside the response`() {
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
            val response: List<Place> = placesApiService.getPlacesByNearLocation("Rotterdam")

            //THEN
            assertTrue(response.isNotEmpty())
        }

    }

    @Test
    fun `getPlacesByNearLocation WHEN FoursquareApiClient give a successful response with an empty list SHOULD return an empty list`() {
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
            val response: List<Place> = placesApiService.getPlacesByNearLocation("Rotterdam")

            //THEN
            assertTrue(response.isEmpty())
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
            val response: Place =
                placesApiService.getPlacesDetails(mockId)

            //THEN
                assertTrue(response.id == mockId)
                assertTrue(response.photos?.isNotEmpty() == true)
        }
    }
}
