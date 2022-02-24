package com.matiasmb.basecode.domain.interactors

import com.matiasmb.basecode.TestData
import com.matiasmb.basecode.TestData.location
import com.matiasmb.basecode.data.repository.PlacesRepository
import com.matiasmb.basecode.domain.interactor.GetPlacesInteractorImpl
import com.matiasmb.basecode.util.Resource
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class GetPlacesInteractorImplTest {

    private lateinit var getPlacesInteractor: GetPlacesInteractorImpl

    @Test
    fun `getFormattedAddress SHOULD return a formatted address WHEN a location object is provided`() {
        // GIVEN
        getPlacesInteractor = GetPlacesInteractorImpl(mock())

        // WHEN
        val formattedLocation = getPlacesInteractor.getFormattedAddress(location)

        // THEN
        assertEquals(
            "Address: Coolsingel 105\nPostal code: 3012 AG\nCity: Rotterdam",
            formattedLocation
        )
    }

    @Test
    fun `transformResult SHOULD return all the contact information formatted WHEN email or telephone or website is provided`() {
        // GIVEN
        getPlacesInteractor = GetPlacesInteractorImpl(mock())

        // WHEN
        val placeItemView = getPlacesInteractor.transformResult(TestData.dataPlaceSearchResponse.results)

        // THEN
        assertTrue(placeItemView?.isNotEmpty() == true)
        assertEquals("De Bijenkorf", placeItemView?.get(0)?.name)
        assertEquals("5b6a9a644ac28a002cf131d0", placeItemView?.get(0)?.id)
    }

    @Test
    fun `fetchPlaceDetails SHOULD return a flow with a place view object WHEN the api response with a success`() {
        // GIVEN
        val placesRepository = mock<PlacesRepository> {
            onBlocking {
                getPlacesByNearLocation(
                    anyString()
                )
            } doReturn TestData.serviceSuccessSearchPlacesResponse
        }
        getPlacesInteractor = GetPlacesInteractorImpl(placesRepository)

        runBlocking {
            // WHEN
            val response = getPlacesInteractor.fetchPlaces("Rotterdam")

            // THEN
            response.collect {
                assertNotNull(it)
                assertEquals("De Bijenkorf", it.data?.get(0)?.name)
                assertEquals("Address: Coolsingel 105\n" +
                        "Postal code: 3012 AG\n" +
                        "City: Rotterdam", it.data?.get(0)?.formattedAddress)
            }
        }
    }

    @Test
    fun `fetchPlaceDetails SHOULD return a flow with a null place view object WHEN the api response with a failure`() {
        // GIVEN
        val placesRepository = mock<PlacesRepository> {
            onBlocking {
                getPlacesByNearLocation(
                    anyString()
                )
            } doReturn TestData.serviceFailureSearchResponse
        }
        getPlacesInteractor = GetPlacesInteractorImpl(placesRepository)

        runBlocking {
            // WHEN
            val response = getPlacesInteractor.fetchPlaces("Rotterdam")

            // THEN
            response.collect {
                assertTrue(it is Resource.Error)
            }
        }
    }
}
