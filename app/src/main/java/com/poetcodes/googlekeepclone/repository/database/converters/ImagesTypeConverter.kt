package com.poetcodes.googlekeepclone.repository.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poetcodes.googlekeepclone.repository.models.Images

class ImagesTypeConverter {

    @TypeConverter
    fun fromImages(images: Images): String {
        return gson.toJson(images)
    }

    @TypeConverter
    fun toImages(imagesString: String): Images {
        val imagesType = object : TypeToken<Images?>() {}.type
        return gson.fromJson(imagesString, imagesType)
    }

    companion object {
        val gson = Gson()
    }

}