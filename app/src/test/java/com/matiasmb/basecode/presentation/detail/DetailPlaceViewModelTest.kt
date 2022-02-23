package com.matiasmb.basecode.presentation.detail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.matiasmb.basecode.CoroutinesRule
import com.matiasmb.basecode.TestData
import com.matiasmb.basecode.TestData.flowNull
import com.matiasmb.basecode.domain.interactor.GetPlaceDetailInteractor
import com.matiasmb.basecode.presentation.ui.detail.DetailPlaceViewModel
import com.matiasmb.basecode.presentation.ui.detail.DetailPlaceViewState
import com.matiasmb.basecode.presentation.ui.detail.DetailPlaceViewState.ViewStateContent.Initial
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class DetailPlaceViewModelTest {

    private lateinit var viewModel: DetailPlaceViewModel

    @get:Rule
    var coroutinesRule = CoroutinesRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `handleOnStart SHOULD update the viewStateContent to LoadData WHEN the interactor return a PlaceView object`() {
        runBlocking {
            launch(Dispatchers.Main) {
                // GIVEN
                val getPlaceDetailInteractor = mock<GetPlaceDetailInteractor> {
                    onBlocking { fetchPlaceDetails(anyString()) } doReturn TestData.placeViewFlow
                }
                val viewState = DetailPlaceViewState(
                    placeId = "mockId",
                    content = Initial
                )
                viewModel = DetailPlaceViewModel(viewState, getPlaceDetailInteractor)

                // WHEN
                viewModel.handleOnStart()

                // THEN
                viewModel.viewStateData.take(1).collect {
                    assertTrue(it.content is DetailPlaceViewState.ViewStateContent.LoadData)
                }
            }
        }
    }

    @Test
    fun `handleOnStart SHOULD update the viewStateContent to LoadData WHEN the interactor return a null PlaceView object`() {
        runBlocking {
            launch(Dispatchers.Main) {
                // GIVEN
                val getPlaceDetailInteractor = mock<GetPlaceDetailInteractor> {
                    onBlocking { fetchPlaceDetails(anyString()) } doReturn flowNull
                }
                val viewState = DetailPlaceViewState(
                    placeId = "mockId",
                    content = Initial
                )
                viewModel = DetailPlaceViewModel(viewState, getPlaceDetailInteractor)

                // WHEN
                viewModel.handleOnStart()

                // THEN
                viewModel.viewStateData.take(1).collect {
                    assertTrue(it.content is DetailPlaceViewState.ViewStateContent.Error)
                }
            }
        }
    }
}
