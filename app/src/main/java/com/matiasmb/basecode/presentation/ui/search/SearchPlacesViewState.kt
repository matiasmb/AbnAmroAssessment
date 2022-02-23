package com.matiasmb.basecode.presentation.ui.search

import com.matiasmb.basecode.domain.model.ItemPlaceView

data class SearchPlacesViewState(val content: ViewStateContent) {

    sealed class ViewStateContent {
        object Initial : ViewStateContent()
        object Loading : ViewStateContent()
        class LoadData(val data: List<ItemPlaceView>) : ViewStateContent()
        object Error : ViewStateContent()
    }
}
