package com.poetcodes.googlekeepclone.repository.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poetcodes.googlekeepclone.repository.models.entities.Label

class LabelTypeConverter {

    @TypeConverter
    fun fromLabel(label: Label): String {
        return gson.toJson(label)
    }

    @TypeConverter
    fun toLabel(labelString: String): Label {
        val labelType = object : TypeToken<Label?>() {}.type
        return gson.fromJson(labelString, labelType)
    }

    companion object {
        val gson = Gson()
    }

}