package com.matiasmb.basecode.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matiasmb.basecode.data.dto.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<Place>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: Place)

    @Query("DELETE FROM places")
    suspend fun deleteAllPlaces()

    @Query("SELECT * FROM places WHERE location LIKE '%' || :nearLocation || '%'")
    fun getPlacesByNearLocation(nearLocation: String): Flow<List<Place>>

    @Query("SELECT * FROM places WHERE id = :placeId")
    fun getPlacesDetails(placeId: String): Flow<Place>
}
