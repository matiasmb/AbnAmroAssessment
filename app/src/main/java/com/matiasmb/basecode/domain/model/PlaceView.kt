package com.matiasmb.basecode.domain.model

data class PlaceView(
    val name: String,
    val formattedAddress: String,
    val description: String?,
    val imageUrl: String?,
    val contactInformation: String,
    val rating: Float?
)
