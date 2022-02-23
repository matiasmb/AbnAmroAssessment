package com.matiasmb.basecode.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.matiasmb.basecode.data.dao.PlacesDao
import com.matiasmb.basecode.data.dto.Place

@Database(entities = [Place::class], version = 1)
@TypeConverters(LocationConverter::class, PhotosConverter::class)
abstract class PlaceDatabase: RoomDatabase() {
    abstract fun placesDao(): PlacesDao
}
