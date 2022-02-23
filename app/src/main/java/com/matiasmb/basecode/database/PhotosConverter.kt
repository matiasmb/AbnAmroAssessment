package com.matiasmb.basecode.database

import androidx.room.TypeConverter
import com.matiasmb.basecode.data.dto.Location
import com.matiasmb.basecode.data.dto.Photo

class PhotosConverter {

    @TypeConverter
    fun stringToPhotos(string: String?): List<Photo> {
        val photos = mutableListOf<Photo>()
        val strings = string?.split("&&")

        strings?.forEach {
            val photoParameter = it.split("##")
            photoParameter.takeIf { photoParameter[0].isNotEmpty() }?.let {
                photos.add(Photo(prefix = photoParameter[0], suffix = photoParameter[1]))
            }
        }

        return photos
    }

    @TypeConverter
    fun photosToString(photos: List<Photo>?): String {
        var outString = ""
        photos?.forEach {
            outString+= "${it.prefix}##${it.suffix}&&"
        }
        return outString
    }
}
