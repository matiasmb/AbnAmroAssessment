package com.matiasmb.basecode.presentation.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matiasmb.basecode.domain.interactor.GetPlaceDetailInteractor
import com.matiasmb.basecode.domain.model.PlaceView
import com.matiasmb.basecode.presentation.ui.detail.DetailPlaceViewState.ViewStateContent.Error
import com.matiasmb.basecode.presentation.ui.detail.DetailPlaceViewState.ViewStateContent.LoadData
import com.matiasmb.basecode.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailPlaceViewModel @Inject constructor(
    private val viewState: DetailPlaceViewState,
    private val getPlaceDetailInteractor: GetPlaceDetailInteractor
) : ViewModel() {

    val viewStateData: StateFlow<DetailPlaceViewState> get() = _viewStateData
    private val _viewStateData: MutableStateFlow<DetailPlaceViewState> = MutableStateFlow(viewState)

    fun handleOnStart() {
        handleInitial()
    }

    private fun handleInitial() {
        viewModelScope.launch {
            getPlaceDetailInteractor.fetchPlaceDetails(viewState.placeId).collect { response ->
                when (response) {
                    is Resource.Error -> response.data?.let { handleLoadPlace(it) } ?: run { handleError() }
                    is Resource.Success -> response.data?.let { handleLoadPlace(it) } ?: run { handleError() }
                }
            }
        }
    }

    private fun handleLoadPlace(placeView: PlaceView) {
        updateViewState { oldState: DetailPlaceViewState ->
            oldState.copy(
                content = LoadData(placeView)
            )
        }
    }

    private fun handleError() {
        updateViewState { oldState: DetailPlaceViewState ->
            oldState.copy(
                content = Error
            )
        }
    }

    private fun updateViewState(reducer: (DetailPlaceViewState) -> DetailPlaceViewState) {
        _viewStateData.value?.let { _viewStateData.value = reducer(it) }
    }
}
