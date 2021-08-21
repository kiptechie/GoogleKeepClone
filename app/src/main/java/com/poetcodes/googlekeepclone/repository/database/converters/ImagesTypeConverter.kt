package com.poetcodes.googlekeepclone.repository.database.converters

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.poetcodes.googlekeepclone.repository.models.Images
import com.poetcodes.googlekeepclone.utils.GsonUtil

class ImagesTypeConverter {

    @TypeConverter
    fun fromImages(images: Images?): String {
        var myImages = images
        if (myImages == null) {
            myImages = Images(ArrayList())
        }
        return GsonUtil.gsonInstance().toJson(myImages)
    }

    @TypeConverter
    fun toImages(imagesString: String?): Images {
        var myImagesString = imagesString
        if (myImagesString == null) {
            myImagesString = ""
        }
        val imagesType = object : TypeToken<Images?>() {}.type
        return GsonUtil.gsonInstance().fromJson(myImagesString, imagesType)
    }

}