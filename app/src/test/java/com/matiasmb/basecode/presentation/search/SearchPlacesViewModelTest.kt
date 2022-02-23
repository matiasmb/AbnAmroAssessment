package com.matiasmb.basecode.presentation.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.matiasmb.basecode.CoroutinesRule
import com.matiasmb.basecode.TestData
import com.matiasmb.basecode.domain.interactor.GetPlacesInteractor
import com.matiasmb.basecode.presentation.ui.search.SearchNavigationCommand
import com.matiasmb.basecode.presentation.ui.search.SearchPlacesIntent
import com.matiasmb.basecode.presentation.ui.search.SearchPlacesViewModel
import com.matiasmb.basecode.presentation.ui.search.SearchPlacesViewState
import com.matiasmb.basecode.presentation.ui.search.SearchPlacesViewState.ViewStateContent.Initial
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class SearchPlacesViewModelTest {

    private lateinit var viewModel: SearchPlacesViewModel

    @get:Rule
    var coroutinesRule = CoroutinesRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `handleIntent SearchTapped SHOULD update the viewStateContent to LoadData WHEN the interactor return a PlaceView object`() {
        runBlocking {
            launch(Dispatchers.Main) {
                // GIVEN
                val getPlacesInteractor = mock<GetPlacesInteractor> {
                    onBlocking { fetchPlaces(anyString()) } doReturn TestData.placeViewListFlow
                }
                val viewState = SearchPlacesViewState(
                    content = Initial
                )
                viewModel = SearchPlacesViewModel(getPlacesInteractor, viewState)

                // WHEN
                viewModel.handleIntent(SearchPlacesIntent.SearchTapped("Rotterdam"))

                // THEN
                viewModel.viewStateData.take(1).collect {
                    assertTrue(it.content is SearchPlacesViewState.ViewStateContent.LoadData)
                }
            }
        }
    }

    @Test
    fun `handleIntent SearchTapped SHOULD update the viewStateContent to LoadData WHEN the interactor return a null PlaceView object`() {
        runBlocking {
            launch(Dispatchers.Main) {
                // GIVEN
                val getPlacesInteractor = mock<GetPlacesInteractor> {
                    onBlocking { fetchPlaces(anyString()) } doReturn TestData.flowNull
                }
                val viewState = SearchPlacesViewState(
                    content = Initial
                )
                viewModel = SearchPlacesViewModel(getPlacesInteractor, viewState)

                // WHEN
                viewModel.handleIntent(SearchPlacesIntent.SearchTapped("Rotterdam"))

                // THEN
                viewModel.viewStateData.take(1).collect {
                    assertTrue(it.content is SearchPlacesViewState.ViewStateContent.Error)
                }
            }
        }
    }

    @Test
    fun `handleIntent PlaceTapped SHOULD call navigate to place details WHEN the intent received is PlaceTapped`() {
        runBlocking {
            launch(Dispatchers.Main) {
                // GIVEN
                val viewState = SearchPlacesViewState(
                    content = Initial
                )
                viewModel = SearchPlacesViewModel(mock(), viewState)

                // WHEN
                viewModel.handleIntent(SearchPlacesIntent.PlaceTapped("mockId"))

                // THEN
                viewModel.navigationSideEffect.take(1).collect {
                    assertTrue(it is SearchNavigationCommand.GoToPlaceDetails)
                }
            }
        }
    }
}
