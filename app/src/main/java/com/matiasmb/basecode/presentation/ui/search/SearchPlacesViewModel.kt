package com.matiasmb.basecode.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matiasmb.basecode.domain.interactor.GetPlacesInteractor
import com.matiasmb.basecode.domain.model.ItemPlaceView
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchPlacesViewModel @Inject constructor(
    private val findProductsUseCase: GetPlacesInteractor,
    private val viewState: SearchPlacesViewState
) : ViewModel() {

    val viewStateData: StateFlow<SearchPlacesViewState> get() = _viewStateData
    private val _viewStateData: MutableStateFlow<SearchPlacesViewState> = MutableStateFlow(viewState)
    private val _navigationSideEffect = Channel<SearchNavigationCommand>(Channel.BUFFERED)
    val navigationSideEffect = _navigationSideEffect.receiveAsFlow()

    fun handleIntent(intent: SearchPlacesIntent) {
        when (intent) {
            is SearchPlacesIntent.PlaceTapped -> handlePlaceTapped(intent.placeId)
            is SearchPlacesIntent.SearchTapped -> handleSearchTapped(intent.nearPlace)
        }
    }

    private fun handleSearchTapped(nearLocation: String) {
        updateViewState { oldState: SearchPlacesViewState ->
            oldState.copy(
                content = SearchPlacesViewState.ViewStateContent.Loading
            )
        }

        viewModelScope.launch(viewModelScope.coroutineContext) {
            findProductsUseCase.fetchPlaces(nearLocation).collect {
                it?.let {
                    handleLoadPlace(it)
                } ?: run {
                    handleError()
                }
            }
        }
    }

    private fun handleLoadPlace(placeList: List<ItemPlaceView>) {
        updateViewState { oldState: SearchPlacesViewState ->
            oldState.copy(
                content = SearchPlacesViewState.ViewStateContent.LoadData(placeList)
            )
        }
    }

    private fun handleError() {
        updateViewState { oldState: SearchPlacesViewState ->
            oldState.copy(
                content = SearchPlacesViewState.ViewStateContent.Error
            )
        }
    }

    private fun handlePlaceTapped(placeId: String) {
        viewModelScope.launch {
            _navigationSideEffect.send(SearchNavigationCommand.GoToPlaceDetails(placeId))
        }
    }

    private fun updateViewState(reducer: (SearchPlacesViewState) -> SearchPlacesViewState) {
        _viewStateData.value.run { _viewStateData.value = reducer(this) }
    }
}
