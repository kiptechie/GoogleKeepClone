package com.poetcodes.googlekeepclone.repository.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poetcodes.googlekeepclone.repository.models.Images

class ImagesTypeConverter {

    @TypeConverter
    fun fromImages(images: Images?): String {
        var myImages = images
        if (myImages == null) {
            myImages = Images(ArrayList())
        }
        return gson.toJson(myImages)
    }

    @TypeConverter
    fun toImages(imagesString: String?): Images {
        var myImagesString = imagesString
        if (myImagesString == null) {
            myImagesString = ""
        }
        val imagesType = object : TypeToken<Images?>() {}.type
        return gson.fromJson(myImagesString, imagesType)
    }

    companion object {
        val gson = Gson()
    }

}