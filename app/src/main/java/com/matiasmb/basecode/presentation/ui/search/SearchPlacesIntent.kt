package com.matiasmb.basecode.presentation.ui.search

sealed class SearchPlacesIntent {
    data class PlaceTapped(val placeId: String): SearchPlacesIntent()
    data class SearchTapped(val nearPlace: String): SearchPlacesIntent()
}
