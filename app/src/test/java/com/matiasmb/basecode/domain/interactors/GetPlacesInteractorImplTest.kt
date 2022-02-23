package com.matiasmb.basecode.domain.interactors

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.matiasmb.basecode.CoroutinesRule
import com.matiasmb.basecode.TestData
import com.matiasmb.basecode.TestData.location
import com.matiasmb.basecode.data.service.PlacesApiService
import com.matiasmb.basecode.domain.interactor.GetPlaceDetailInteractorImpl
import com.matiasmb.basecode.domain.interactor.GetPlacesInteractorImpl
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class GetPlacesInteractorImplTest {

    @get:Rule
    var coroutinesRule = CoroutinesRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

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
        assertTrue(placeItemView.isNotEmpty())
        assertEquals("De Bijenkorf", placeItemView[0].name)
        assertEquals("5b6a9a644ac28a002cf131d0", placeItemView[0].id)
    }

    @Test
    fun `fetchPlaceDetails SHOULD return a flow with a place view object WHEN the api response with a success`() {
        // GIVEN
        val placesApiService = mock<PlacesApiService> {
            onBlocking {
                getPlacesByNearLocation(
                    anyString()
                )
            } doReturn TestData.serviceSuccessSearchPlacesResponse
        }
        getPlacesInteractor = GetPlacesInteractorImpl(placesApiService)

        runBlocking {
            // WHEN
            val response = getPlacesInteractor.fetchPlaces("Rotterdam")

            // THEN
            response.collect {
                assertNotNull(it)
                assertEquals("De Bijenkorf", it?.get(0)?.name)
                assertEquals("Address: Coolsingel 105\n" +
                        "Postal code: 3012 AG\n" +
                        "City: Rotterdam", it?.get(0)?.formattedAddress)
            }
        }
    }

    @Test
    fun `fetchPlaceDetails SHOULD return a flow with a null place view object WHEN the api response with a failure`() {
        // GIVEN
        val placesApiService = mock<PlacesApiService> {
            onBlocking {
                getPlacesByNearLocation(
                    anyString()
                )
            } doReturn TestData.serviceFailureResponse
        }
        getPlacesInteractor = GetPlacesInteractorImpl(placesApiService)

        runBlocking {
            // WHEN
            val response = getPlacesInteractor.fetchPlaces("Rotterdam")

            // THEN
            response.collect {
                assertNull(it)
            }
        }
    }
}