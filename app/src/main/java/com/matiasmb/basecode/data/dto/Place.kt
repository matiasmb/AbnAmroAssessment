package com.matiasmb.basecode.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "places")
data class Place(
    @SerializedName("fsq_id")
    @PrimaryKey
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
