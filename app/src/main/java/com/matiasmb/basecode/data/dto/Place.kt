package com.matiasmb.basecode.data.dto

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("fsq_id")
    val id: String,
    val name: String,
    val location: Location,
    val description: String?,
    val photos: List<Photo>?,
    val rating: Float?,
    val tel: String?,
    val email: String?,
    val website: String?
)
