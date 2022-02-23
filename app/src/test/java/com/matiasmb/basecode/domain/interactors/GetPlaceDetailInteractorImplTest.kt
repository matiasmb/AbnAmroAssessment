package com.matiasmb.basecode.domain.interactors

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.matiasmb.basecode.CoroutinesRule
import com.matiasmb.basecode.TestData
import com.matiasmb.basecode.TestData.location
import com.matiasmb.basecode.data.repository.PlacesRepository
import com.matiasmb.basecode.domain.interactor.GetPlaceDetailInteractorImpl
import com.matiasmb.basecode.util.Resource
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class GetPlaceDetailInteractorImplTest {

    @get:Rule
    var coroutinesRule = CoroutinesRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var getPlaceDetailInteractor: GetPlaceDetailInteractorImpl

    @Test
    fun `getMainPhoto SHOULD return the image url for the first photo WHEN a non empty list of photos is provided`() {
        // GIVEN
        getPlaceDetailInteractor = GetPlaceDetailInteractorImpl(mock())

        // WHEN
        val photoList = getPlaceDetailInteractor.getMainPhoto(TestData.photoListDto)

        // THEN
        assertNotNull(photoList)
    }

    @Test
    fun `getMainPhoto SHOULD return null WHEN an empty list of photos is provided`() {
        // GIVEN
        getPlaceDetailInteractor = GetPlaceDetailInteractorImpl(mock())

        // WHEN
        val photoList = getPlaceDetailInteractor.getMainPhoto(emptyList())

        // THEN
        assertNull(photoList)
    }

    @Test
    fun `getFormattedAddress SHOULD return a formatted address WHEN a location object is provided`() {
        // GIVEN
        getPlaceDetailInteractor = GetPlaceDetailInteractorImpl(mock())

        // WHEN
        val formattedLocation = getPlaceDetailInteractor.getFormattedAddress(location)

        // THEN
        assertEquals(
            "Address: Coolsingel 105\nPostal code: 3012 AG\nCity: Rotterdam",
            formattedLocation
        )
    }

    @Test
    fun `getContactInformation SHOULD return all the contact information formatted WHEN email or telephone or website is provided`() {
        // GIVEN
        getPlaceDetailInteractor = GetPlaceDetailInteractorImpl(mock())

        // WHEN
        val formattedContactInformation = getPlaceDetailInteractor.getContactInformation(
            "mockEmail",
            "mockTelephone",
            null
        )

        // THEN
        assertEquals("Telephone: mockTelephone\nEmail: mockEmail\n", formattedContactInformation)
    }

    @Test
    fun `transformResult SHOULD return all the contact information formatted WHEN email or telephone or website is provided`() {
        // GIVEN
        getPlaceDetailInteractor = GetPlaceDetailInteractorImpl(mock())

        // WHEN
        val placeView = getPlaceDetailInteractor.transformResult(TestData.placeDetailSearch)

        // THEN
        assertEquals("De Bijenkorf", placeView?.name)
        assertEquals(
            "Telephone: 010 282 3700\nEmail: testEmail\nWebsite: http://www.debijenkorf.nl",
            placeView?.contactInformation
        )
        assertTrue(placeView?.rating is Float)
    }

    @Test
    fun `fetchPlaceDetails SHOULD return a flow with a place view object WHEN the api response with a success`() {
        // GIVEN
        val placesRepository = mock<PlacesRepository> {
            onBlocking {
                getPlacesDetail(
                    anyString()
                )
            } doReturn TestData.serviceSuccessDetailPlaceResponse
        }
        getPlaceDetailInteractor = GetPlaceDetailInteractorImpl(placesRepository)

        runBlocking {
            // WHEN
            val response = getPlaceDetailInteractor.fetchPlaceDetails("5b6a9a644ac28a002cf131d0")

            // THEN
            response.collect {
                assertEquals("De Bijenkorf", it.data?.name)
                assertEquals(
                    "Swatch, launched in 1983 by Nicolas G. Hayek, is a leading Swiss watch maker and one of the world's most popular brands",
                    it.data?.description
                )
                assertEquals(9.5F, it.data?.rating)
            }
        }
    }

    @Test
    fun `fetchPlaceDetails SHOULD return a flow with a null place view object WHEN the api response with a failure`() {
        // GIVEN
        val placesRepository = mock<PlacesRepository> {
            onBlocking {
                getPlacesDetail(
                    anyString()
                )
            } doReturn TestData.serviceFailureDetailResponse
        }
        getPlaceDetailInteractor = GetPlaceDetailInteractorImpl(placesRepository)

        runBlocking {
            // WHEN
            val response = getPlaceDetailInteractor.fetchPlaceDetails("5b6a9a644ac28a002cf131d0")

            // THEN
            response.collect {
                assertTrue(it is Resource.Error)
            }
        }
    }
}
