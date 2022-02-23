package com.matiasmb.basecode.presentation.ui.detail

import com.matiasmb.basecode.domain.model.PlaceView

data class DetailPlaceViewState(val placeId: String, val content: ViewStateContent) {

    sealed class ViewStateContent {
        object Initial : ViewStateContent()
        class LoadData(val data: PlaceView) : ViewStateContent()
        object Error : ViewStateContent()
    }
}
