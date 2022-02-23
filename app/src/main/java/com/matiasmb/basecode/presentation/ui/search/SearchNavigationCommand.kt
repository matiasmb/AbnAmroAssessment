package com.matiasmb.basecode.presentation.ui.search

sealed class SearchNavigationCommand {
    data class GoToPlaceDetails(val placeId: String) : SearchNavigationCommand()
}
