package com.matiasmb.basecode.database

import androidx.room.TypeConverter
import com.matiasmb.basecode.data.dto.Location

class LocationConverter {

    @TypeConverter
    fun stringToLocation(string: String?): Location? {
        val strings = string?.split("##")
        return strings?.let {
            Location(strings[0], strings[1], strings[2])
        } ?: run { null }
    }

    @TypeConverter
    fun locationToString(location: Location): String {
        return "${location.address}##${location.postcode}##${location.locality}"
    }
}
